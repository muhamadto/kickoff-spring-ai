# Exercise: Build It Yourself Before Opening [012-agentic-rag](../012-agentic-rag)

Every grounding technique so far, `005-tool-calling` through `011-hybrid-search-rag`, answers questions:
retrieve or compute something, then tell the fan about it. Nothing yet lets the model take an action with a real-world side effect on the fan's
behalf. Before you look at `012-agentic-rag`, try to add a booking capability yourself. The
[Tool Calling](https://docs.spring.io/spring-ai/reference/api/tools.html) documentation covers everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`012-agentic-rag` sub-module.

---

## The Challenge

### Task 1 - Add a tool with a side effect

Write a `@Component` with one `@Tool`-annotated method, `bookMatchTicket`, taking the home team, away team, date and a ticket quantity as `@ToolParam`
s. There's no real ticketing system to call, so have it log the booking and return a generated reference string.

Think about: how is a "book this" tool different, in what it's for, from every `@Tool` or MCP tool
`005-tool-calling` and `007-mcp-server` exposed?

### Task 2 - Register it alongside retrieval, not instead of it

Add your tool to `defaultTools(...)` on the same `ChatClient` that still carries
`HybridSearchAdvisor`
in its advisor chain. Write a system prompt line telling the model when to call the tool.

Think about: should retrieval still run on every request, even a booking request? What would break if `HybridSearchAdvisor` stopped running whenever
the model decided to call your tool instead?

### Task 3 - Watch the model decide, and get it wrong

Ask something that only mentions a match in passing ("I'd love to see Morocco play"). Watch
`model.tool: DEBUG`: does your tool fire anyway? Then ask a genuine booking request phrased ambiguously (a nickname instead of a full team name, "the
game this weekend" instead of a date). Does the model still call the tool, guessing at the missing details?

Think about: what part of the tool's description would you sharpen to fix either failure, and is a better description alone ever a complete fix for a
decision the model gets to make?

### Task 4 - Ask it to book something that never happened, or book it twice

Ask for a fixture that was never played, or for the exact same booking twice in one conversation. Does your tool still return a confident reference
either time?

Think about: could the tool itself refuse these, or does that decision belong somewhere that runs before the model is ever involved?

### Task 5 - Decide what you'd trust the model with

For each rough edge you found, write down whether you'd fix it with a better prompt or tool description, or with code that enforces the outcome
regardless of what the model decides. You don't need to build the fix, just decide which category each one falls into, before you open
`012-agentic-rag`.

---

## Done?

Open [012-agentic-rag](../012-agentic-rag) and compare. Pay attention to how deliberately unguarded
`BookingTool` is left, on purpose: the rough edges you just found are the point, and
`013-guarded-rag` is where they get fixed.

Also notice retrieval itself changed underneath: `012-agentic-rag` reverts to `010-vector-store-rag`'s plain `QuestionAnswerAdvisor` rather than
carrying this module's hand-rolled fusion forward (see its `GenAiConfig` for the reason). Keeping `HybridSearchAdvisor` in your own solution is fine;
the tool-calling decision is this exercise's actual point, not which retrieval advisor sits underneath it.
