# Exercise: Build It Yourself Before Opening [006-tool-search](../006-tool-search)

Every one of your thirteen tools ships its full schema, name, description, every parameter's description, on every single request, whether the fan's
question needs one of them or none at all. Before you look at `006-tool-search`, try to fix that yourself using
[Spring AI's Tool Search Tool](https://spring.io/blog/2025/12/11/spring-ai-tool-search-tools-tzolov):
instead of sending every tool definition upfront, give the model one tool, a search over the real ones, and let it pull in only what it decides it
needs.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`006-tool-search` sub-module.

---

## The Challenge

### Task 1 - Confirm the problem exists

Turn on request logging at the HTTP client level (or just trust the `advisor: DEBUG` log's tool list) and look at what actually goes out on a request
to `/venues?city=Dallas`. Count how many tool schemas are in that payload versus how many the question actually needed.

Think about: what happens to that count as this module's thirteen tools become fifty? A hundred?

### Task 2 - Index your tools instead of sending them directly

Add `spring-ai-tool-search-advisor` (not the starter: check what `spring-ai-bom` actually manages before reaching for
`spring-ai-starter-tool-search-advisor`). Build a `ToolSearchToolCallingAdvisor`
with an in-memory `LuceneToolIndex`, and register it as a `defaultAdvisor` alongside your existing
`defaultTools`.

Think about: does adding the search advisor mean removing `defaultTools`, or keeping it? What is the advisor actually replacing?

### Task 3 - Get the ordering right

Wire the advisor in without setting an explicit order and call an endpoint that needs
`PiiRedactionAdvisor` to actually redact something first.

Think about: `ToolCallingAdvisor.DEFAULT_ORDER` is `Integer.MIN_VALUE + 300`. What does that mean for when the tool-search loop runs relative to
advisors declared with small positive orders like
`PiiRedactionAdvisor(10)`? Fix it, and justify the number you chose relative to `10`, `20` and `30`.

### Task 4 - Watch the extra round trip

Call the same `/venues?city=Dallas` request from Task 1 and watch `advisor: DEBUG` again.

Think about: what's the new call that appears before the named tool call? Is it a tool call the model chose to make, or something else?

### Task 5 - Decide when this is worth it

This module has thirteen tools. Spring AI's own benchmarks report roughly one extra request per conversation turn against a 34-64% reduction in total
tokens, measured against real integrations with dozens of tools.

Think about: at thirteen tools, is the extra round trip worth the token savings here specifically? What tool count, or what kind of tool catalog, would
change your answer?

---

## Helping Material

* [Tool Calling]([https://docs.spring.io/spring-ai/reference/api/tools.html](https://docs.spring.io/spring-ai/reference/api/tools.html#_spring_boot_auto_configuration))
* [Spring AI Tool Search Tool (Tzolov)](https://spring.io/blog/2025/12/11/spring-ai-tool-search-tools-tzolov)
* [Anthropic - Code execution with MCP: Building more efficient agents](https://www.anthropic.com/engineering/advanced-tool-use)
* [Anthropic - Code execution with MCP: Building more efficient agents](https://www.anthropic.com/engineering/code-execution-with-mcp)

---

## Done?

Open [006-tool-search](../006-tool-search) and compare. Pay attention to what stayed identical (every `@Tool` class, every endpoint) and what changed
(one bean, one dependency, nothing in the
`tool` package at all).
