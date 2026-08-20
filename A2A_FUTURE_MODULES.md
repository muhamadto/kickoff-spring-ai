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

## Dedicated `016-a2a-server`/`016-a2a-client` pair

Mirror the `007-mcp-server`/`007-mcp-client` split rather than pointing at a real external agent, to keep the whole series self-contained:

- **`016-a2a-server`**: publishes an `AgentCard`, handles `message/send` and `tasks/get` for one narrow skill (a natural fit: the booking flow already
  established in `012-agentic-rag` /
  `013-guarded-rag`).
- **`016-a2a-client`** (or folded into the main app): resolves the server's `AgentCard`, and exposes delegation to it as a single `@Tool` — e.g.
  `delegateToBookingAgent(...)` — so from the model's point of view it's still just a tool call. The tool's implementation does the full round trip:
  resolve/cache the `AgentCard`, build `MessageSendParams`, call `SEND_MESSAGE_METHOD`, then poll or subscribe (`SUBSCRIBE_TO_TASK_METHOD`) until the
  `Task` reaches a terminal state, and return the artifact content as the tool's result. Same shape as `007-mcp-client`'s MCP tools; the difference is
  what's behind the call is a whole autonomous remote agent instead of a function or an MCP server.

## Where guardrails apply

Treat a remote agent's output with at least as much suspicion as any other tool result — arguably more, since its prompting isn't under this
application's control. Applying `013-guarded-rag`'s pattern here means: classify/validate *before* delegating (should this question even go to the
remote agent?), and validate/sanitize what comes back *before* it re-enters this application's own model context.

## Open questions for whoever picks this up

- Has Spring AI (or the A2A project) shipped Spring integration yet? Check `spring-ai-bom` for an
  `a2a` artifact before writing the Spring MVC adapter by hand — a starter would remove that plumbing work entirely rather than just being
  nice-to-have.
- Is a single booking-delegation skill enough to teach the protocol, or does demonstrating multi-turn `input-required` task negotiation need a second
  skill?
- Streaming (`message/stream` + SSE) vs. polling `tasks/get` — worth showing both, or is polling enough for a first pass?
