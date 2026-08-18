# Exercise: Build It Yourself Before Opening [007-mcp-client](../007-mcp-client) and [007-mcp-server](../007-mcp-server)

Your grounding tools live inside the application: only this app can use them. Before you look at
`007-mcp-server` and `007-mcp-client`, try to move the grounding into a standalone server that any client can share, using the Model Context Protocol.
The
[MCP overview](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html),
[server boot starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html),
[client boot starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html)
and [server annotations](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-annotations-server.html)
documentation cover everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`007-mcp-server` and `007-mcp-client` sub-modules.

---

## The Challenge

### Task 1 - Pick a transport

The server starter supports SSE, Streamable HTTP and Stateless Streamable HTTP over the same
`spring-ai-starter-mcp-server-webmvc` dependency.

Think about: which one is the current MCP specification transport, which one is legacy, and what does the stateless variant give up? What single yaml
property switches between them?

### Task 2 - Serve tools

Create a new Spring Boot app that exposes this module's grounding data as MCP tools using
`@McpTool` and `@McpToolParam`: fixtures for a date, the result of a played match, stadium details, group standings.

Think about: your fan assistant already registers in-process tools named `get-venue` and
`get-fixtures`. What happens if the server publishes tools with the same names and a client registers both?

### Task 3 - Serve resources

Expose the same data as resources with `@McpResource` and URI templates, e.g.
`worldcup://teams/{team}/squad`.

Think about: a tool and a resource can return identical data. Who decides when a tool runs, and who decides when a resource is attached? Why does that
difference matter?

### Task 4 - Serve prompts and completions

Publishes `match-recap(fixture)` prompt with `@McpPrompt` and `@McpArg`, then add
`@McpComplete` handlers that autocomplete its arguments from your data.

Think about: in 002 the prompt templates lived inside the application. What changes when the server owns them?

### Task 5 - Consume it all

Back in the fan assistant, add `spring-ai-starter-mcp-client`, connect to your server, and add one endpoint per capability. Keep every existing
endpoint untouched.

Think about:

* How do the server's tools become Spring AI `ToolCallback`s? Which auto-configured bean does the translation?
* Give the MCP tools their own `ChatClient` bean without the in-process tools. Call your new grounded endpoint and the existing `/matches` with the
  same date. Which one tells the truth, and how can you prove where the answer came from?
* Can your application context (and its test) still start when the MCP server is down?

---

## Done?

Open [007-mcp-server](../007-mcp-server) and [007-mcp-client](../007-mcp-client) and compare. Then point `npx @modelcontextprotocol/inspector` at your
own server and watch the completions fire as you type.
