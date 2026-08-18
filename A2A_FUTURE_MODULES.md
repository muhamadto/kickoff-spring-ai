# Future Work: Agent-to-Agent (A2A) Modules

Status: not started. This series' intro lists A2A as an assumed-knowledge concept (`05. A2A`) but there's no module demonstrating it yet, unlike Tools
and MCP which each get a full pair (`006`/
`006-tool-search`, `007-mcp-server`/`007-mcp-client`). This document is the design sketch to pick back up when that gap is worth closing.

## Why this hasn't started

Spring AI has no A2A starter — `spring-ai-bom:2.0.0` doesn't manage any `a2a` artifact. But that doesn't actually block building this: A2A is a
transport-level protocol (JSON-RPC over HTTP), entirely independent of any AI framework. The official Java SDK (`org.a2aproject.sdk:a2a-java-sdk-*`,
`1.2.0.Final`, resolvable from Maven Central) splits cleanly into a framework-agnostic core and one Quarkus-specific adapter:

- `a2a-java-sdk-server-common` — `RequestHandler`, `DefaultRequestHandler`, `AgentExecutor`,
  `TaskManager`, `TaskStore`/`InMemoryTaskStore`, `QueueManager`. Plain Java, no Quarkus dependency anywhere.
- `a2a-java-sdk-reference-jsonrpc` — `org.a2aproject.sdk.server.apps.quarkus.*`. This is *only* the HTTP routing glue: a Quarkus JAX-RS endpoint that
  deserializes the JSON-RPC request and calls
  `RequestHandler.handle(...)`.

Nothing stops writing the Spring MVC equivalent of that second module: a `@RestController` that deserializes the JSON-RPC POST body and delegates to
`DefaultRequestHandler`, same on the client side with `ClientTransport`/`JSONRPCTransportProvider` called from a plain `@Service`. So this module is
buildable today with plain Spring Boot — the only place Spring AI enters at all is the very last step, wrapping the delegation call as an `@Tool` so
the model can invoke it, and that wrapper is a few lines regardless of whether a starter exists. The real reason to hold off is smaller than
"blocked": writing that Spring MVC adapter yourself is real, un-abstracted plumbing work, in a series that's otherwise entirely "here's the Spring AI
abstraction for X" — worth weighing against the teaching value before committing to it, not waiting on Spring AI to catch up.

## What the SDK actually gives you

Verified against the real artifacts, not the README's prose:

- **Core types** (`a2a-java-sdk-spec`): `AgentCard`, `AgentSkill`, `Task`, `TaskStatus`,
  `MessageSendParams`, `MessageSendConfiguration`, `DataPart`/`FilePart`.
- **Method names** (`A2AMethods`): `SEND_MESSAGE_METHOD`, `SEND_STREAMING_MESSAGE_METHOD`,
  `GET_TASK_METHOD`, `CANCEL_TASK_METHOD`, `LIST_TASK_METHOD`, `SUBSCRIBE_TO_TASK_METHOD`, plus push-notification-config get/set/delete.
- **Discovery** (`a2a-java-sdk-http-client`): `A2ACardResolver` fetches the target agent's
  `AgentCard`, normally published at `/.well-known/agent.json`.
- **Transport** (`a2a-java-sdk-client-transport-jsonrpc` / `-spi`): `ClientTransport`,
  `JSONRPCTransportProvider` — the layer a Spring integration would eventually wrap.

## Two ways to build this

**Option A**, below, is a dedicated module pair, model-driven via `@Tool` — the closer match to how
`007-mcp-server`/`007-mcp-client` introduced MCP as its own thing. **Option B** is cheaper: no new module, delegate directly out of an existing
module's already-deterministic booking branch. They teach slightly different lessons (model-driven delegation vs. code-driven delegation) and either
is a reasonable place to start.

### Option A: dedicated `016-a2a-server`/`016-a2a-client` pair

Mirror the `007-mcp-server`/`007-mcp-client` split rather than pointing at a real external agent, to keep the whole series self-contained:

- **`016-a2a-server`**: publishes an `AgentCard`, handles `message/send` and `tasks/get` for one narrow skill (a natural fit: the booking flow already
  established in `012-agentic-rag` /
  `013-guarded-rag`).
- **`016-a2a-client`** (or folded into the main app): resolves the server's `AgentCard`, and exposes delegation to it as a single `@Tool` — e.g.
  `delegateToBookingAgent(...)` — so from the model's point of view it's still just a tool call. The tool's implementation does the full round trip:
  resolve/cache the `AgentCard`, build `MessageSendParams`, call `SEND_MESSAGE_METHOD`, then poll or subscribe (`SUBSCRIBE_TO_TASK_METHOD`) until the
  `Task` reaches a terminal state, and return the artifact content as the tool's result. Same shape as `007-mcp-client`'s MCP tools; the difference is
  what's behind the call is a whole autonomous remote agent instead of a function or an MCP server.

### Option B: delegate directly from `015-observability`, no new module

`015-observability` is the better host for this than an earlier module, for a reason specific to what it teaches: everything it currently does gets
traced *for free*, because every hop is a Spring AI call (`ChatClient`, `VectorStore`) and Spring AI instruments those itself once Micrometer and a
tracer are on the classpath. An A2A delegation call is a plain HTTP round trip made through the A2A SDK's own client, with no Spring AI awareness at
all — it would produce **no span** unless something wraps it deliberately. That gap is exactly the module's own point generalised: Spring AI's
auto-instrumentation covers Spring AI's own surface, and anything outside that surface (a raw JSON-RPC call to another agent, here) is on you to
instrument.

The seam to build on is `Worldcup2026Service.respond(...)`'s `BOOKING_REQUEST` branch:

```java
case BOOKING_REQUEST ->bookingService.

book(conversationId, extractBookingRequest(question));
```

`bookingService.book(...)` already receives a `BookingRequest` that's been through classification and structured extraction — nothing upstream of this
line changes. The change is what handles the request once it's known to be a genuine, well-formed booking:

1. **Dependencies.** Add `org.a2aproject.sdk:a2a-java-sdk-spec`, `-http-client`,
   `-client-transport-jsonrpc`, `-client-transport-spi` to `pom.xml` (none are BOM-managed, same situation `lucene-core` was in for
   `006-tool-search` — pin versions explicitly and verify they resolve before writing code against them).
2. **New class, `ai.spring.learning.worldcup.a2a.BookingAgentClient`** (`@Component`), holding: the target agent's base URL as a config property
   (`worldcup.a2a.booking-agent-url`, mirroring how
   `007-mcp-client` configures its MCP connection), a cached `AgentCard` resolved once via
   `A2ACardResolver` against `{base-url}/.well-known/agent.json`, and a `ClientTransport` obtained from `JSONRPCTransportProvider`.
3. **One method**, matching `BookingService.book(...)`'s exact signature so the call site barely changes:
   `String book(String conversationId, BookingRequest request)`. Internally: build a
   `Message`/`MessageSendParams` from the request's fields, send it via
   `A2AMethods.SEND_MESSAGE_METHOD`, poll `GET_TASK_METHOD` (simplest first pass;
   `SUBSCRIBE_TO_TASK_METHOD` later) until the returned
   `Task` reaches a terminal `TaskState`, then return the artifact's text.
4. **Wrap the call in a manual `Observation`.** `ObservationRegistry` is already auto-configured because Actuator and a tracer are already on this
   module's classpath; inject it into
   `BookingAgentClient` and wrap the send/poll round trip in an observation (name it in the same style Spring AI uses, e.g. `a2a.client.delegate`), so
   the resulting trace shows a span for the remote hop too instead of a silent gap between the classification span and the response being logged.
5. **Swap the call site.** `Worldcup2026Service` gets a `BookingAgentClient` alongside
   `BookingService`
   (constructor injection, same pattern as every other dependency in that class) and the
   `BOOKING_REQUEST`
   branch calls it instead. Nothing else in `respond(...)`, `chat(...)`, or the controller changes —
   `chatHistoryRepository.record(...)` already logs whatever string comes back, so the audit trail covers the delegated path with zero additional
   code.
6. **Fallback, optional but consistent with this series' style.** `007-mcp-client` starts fine with no live MCP server by resolving an empty tool
   list; do the same here — catch the SDK's
   `A2AClientException` around the round trip and fall back to the local `BookingService.book(...)`
   if the remote agent is unreachable, rather than failing the whole request.

One thing specific to Option B: `book(...)`'s return value goes straight into
`chatHistoryRepository.record(...)` and back to the fan as-is, with no model in between to give it a second look. See "Where guardrails apply" below —
it applies here even more directly than in Option A.

## Where guardrails apply, either way

Treat a remote agent's output with at least as much suspicion as any other tool result — arguably more, since its prompting isn't under this
application's control. Applying `013-guarded-rag`'s pattern here means: classify/validate *before* delegating (should this question even go to the
remote agent? — already true by construction in Option B, since the branch is only reached after classification), and validate/sanitize what comes
back *before* it reaches the fan or, in Option A, re-enters this application's own model context.

## Open questions for whoever picks this up

- Has Spring AI (or the A2A project) shipped Spring integration yet? Check `spring-ai-bom` for an
  `a2a` artifact before writing the Spring MVC adapter by hand — a starter would remove that plumbing work entirely rather than just being
  nice-to-have.
- Is a single booking-delegation skill enough to teach the protocol, or does demonstrating multi-turn `input-required` task negotiation need a second
  skill?
- Streaming (`message/stream` + SSE) vs. polling `tasks/get` — worth showing both, or is polling enough for a first pass?
