# 007-mcp-server: Spring AI

> [!CAUTION]
> Very sharp tool, do *not* cut yourself, vulnerable to
> * Naming attacks
> * Shadowing attacks
> * Context poisoning
> * Rug pulls

A standalone Model Context Protocol (MCP) server exposing World Cup 2026 data. Unlike every other module in this series, this is not the fan-facing
application and carries no REST endpoints; its entire surface is MCP capabilities, consumed by `007-mcp-client`.

The point of the module: in `005-tool-calling` the grounding data lived inside the application, as separate `@Tool` classes. Here that same dataset
moves into a standalone server that any MCP client can share, so "where did this data come from" has exactly one answer across every client that
connects: MCP server, always.

---

## 1. Configuration

One starter provides all HTTP transports:

```xml

<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

```yaml
spring:
  ai:
    mcp:
      server:
        protocol: streamable
        annotation-scanner:
          enabled: true
        capabilities:
          tool: true
          resource: true
          prompt: true
          completion: true
```

`STREAMABLE` is the current MCP transport and the one this tutorial teaches. The alternatives:

* `STATELESS`: the serverless variant. Simplest, but gives up sessions and server-initiated messages (change notifications, sampling). A one-line
  switch if you deploy to Lambda-style infrastructure.
* `SSE`: the legacy HTTP transport, kept for older clients. The MCP specification deprecated it in favour of Streamable HTTP.

The server listens on port `8085` with the MCP endpoint at `/mcp`. All four capability types are enabled and discovered by the annotation scanner.

---

## 2. Source Code

### The dataset

The `data` package holds eight plain data classes (`TeamData`, `VenueData`, `FixtureData`,
`MatchData`, `RefereeData`, `StandingsData`, `PlayerData`, `NewsData`), the same 2026 World Cup data as `005-tool-calling`'s. Every capability below
is a different access pattern over this data.

### Tools (`capability/TournamentTools.java`)

The model suggests when to call these **(the model never calls them)**. Thirteen tools, one `@McpTool` method per question, same names as
`005-tool-calling`'s tools:

| Tool                     | Purpose                                                      |
|--------------------------|--------------------------------------------------------------|
| `get-teams`              | Teams in a group, or all 48                                  |
| `get-venue`              | Stadium, city, country and capacity for a host city          |
| `get-fixtures`           | Fixtures for a date or a team                                |
| `get-match-result`       | Final score of a played match                                |
| `get-match-stats`        | Man of the Match, cards, shots for a played match            |
| `get-referees`           | Head referees appointed for the tournament                   |
| `get-group-standings`    | Final group-stage standings table                            |
| `get-team-journey`       | A team's fixtures so far, in order, with results             |
| `get-player`             | A player's goals, assists, cards and Man of the Match awards |
| `get-goal-leaderboard`   | Top goal scorers so far                                      |
| `get-assist-leaderboard` | Top assist providers so far                                  |
| `get-news`               | News and incidents about a team                              |
| `get-controversies`      | Controversial incidents from the group stage                 |

### Resources (`capability/TournamentResources.java`)

The user or client application decides when to attach these; the model never calls them:

| URI                                   | Content               |
|---------------------------------------|-----------------------|
| `worldcup://fixtures/{date}`          | A day's fixtures      |
| `worldcup://teams/{team}/squad`       | A team's full squad   |
| `worldcup://stadiums`                 | The stadium directory |
| `worldcup://groups/{group}/standings` | Final group tables    |

Same data as some of the tools above, opposite control model: that contrast is the lesson.

### Prompts (`capability/TournamentPrompts.java`)

One reusable template the server owns, registered with `@McpPrompt` and `@McpArg`:

* `match-recap(fixture)`: the prompt for a full recap of a played match.

Earlier modules kept prompt templates inside the application; publishing this one here gives every connected client one version instead of each
writing its own.

### Completions (`capability/TournamentCompletions.java`)

Autocomplete for prompt arguments and resource URI variables, registered with `@McpComplete`. Completions suit small, finite sets, which a tournament
provides for free:

| Completion             | Completes                                                      |
|------------------------|----------------------------------------------------------------|
| `completeRecapFixture` | The `match-recap` prompt's `fixture` argument                  |
| `completeSquadTeam`    | The `worldcup://teams/{team}/squad` resource's `team` variable |

---

## 3. Running and Testing

### Running

```bash
./mvnw spring-boot:run
```

### Testing

Explore the server without writing a client using the MCP inspector:

> [!WARNING]
> If you are working on a machine provided by your employer, this inspector is most likely not allowed.
> Please consult your company's policy, your manager and your security team before you install it.

```bash
npx @modelcontextprotocol/inspector
```

Connect it to `http://localhost:8085/mcp` with transport type Streamable HTTP, then browse the tools, read resources, run the prompt and watch the
completions fire as you type arguments.

Another way to explore the service is to move on to `007-mcp-client`, which consumes all four capabilities.

---

## 4. References

* [MCP Server Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html)
* [Streamable HTTP](https://modelcontextprotocol.io/specification/2025-06-18/basic/transports#streamable-http)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)
