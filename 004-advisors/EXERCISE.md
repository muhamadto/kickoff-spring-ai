# Exercise: Build It Yourself Before Opening [005-tool-calling](../005-tool-calling)

Every endpoint so far leaves grounding to Gemini's own live search (`google-search-retrieval` in the yaml): often accurate, but a judgement call the
model makes on its own, not a lookup against data you control, and there is no way to tell from a plain text answer whether it actually searched.
Before you look at `005-tool-calling`, try to ground one question deterministically with your own tool instead.
The [tool calling documentation](https://docs.spring.io/spring-ai/reference/api/tools.html)
covers everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`005-tool-calling` sub-module.

---

## The Challenge

### Task 1 - Write a tool

Create a `VenueTool` component with a `@Tool` method that returns venue details for a host city. The method should take a city name and return a
`Venue` record with the stadium name, city, country, and capacity. Use `@ToolParam` to describe each parameter so the model knows what to pass.

Think about: what does the model see? How does it know the tool exists and what it does?

### Task 2 - Wire the tool

Register the tool as a `defaultTools` on every `ChatClient` in `GenAiConfig`. Add an endpoint that asks the model about a venue and let the model
decide to call the tool. Watch the
`SimpleLoggerAdvisor` output - can you see the tool invocation request and response in the log?

Think about: what happens if you register the tool on only one client? Which endpoints can use it?

### Task 3 - Compare search-grounded vs tool-grounded

Call `/fixtures?team=Morocco` (search grounding only, the model's own judgement call) and a new endpoint with tools that returns the same team's
fixtures. What is different in the answers? Can you tell, from the response alone, which one you can actually trust?

Think about: the model still does not know match results. What would you need to add a tool for to stop it guessing scores?

### Task 4 - A second tool

Add a `get-fixtures` tool that returns a list of `Fixture` records, filtered by an optional date and an optional team. The model should be able to
call it when the fan asks about fixtures on a specific date or for a specific team. What happens if the fan asks about a date with no fixtures?

Think about: how does the model decide which tool to call? What if both tools could answer the question?

### Task 5 - Tool calling with structured output

Combine tool calling with structured output. Add an endpoint that asks about a venue and returns a
`Venue` record via `entity(Venue.class)`. Does the tool result get mapped into the record, or does the model still generate its own JSON?

Think about: what is the difference between the tool returning a `Venue` record and the model generating a `Venue` from the tool's text answer?

---

## Helping Material

* [Tool Calling](https://docs.spring.io/spring-ai/reference/api/tools.html)

---

## Done?

Open [005-tool-calling](../005-tool-calling) and compare. It splits grounding into one `@Tool`
class per concern (`VenueTool`, `FixtureTool`, `MatchTool`, and more) rather than one big class. Pay attention to how they are all wired as
`defaultTools` in `GenAiConfig`, and how the
`advisor: DEBUG` log shows the model invoking a tool before answering.