# Exercise: Build It Yourself Before Opening [003-structured-output](../003-structured-output)

Every endpoint so far returns the model's prose as a `String`. Callers can't rely on prose - they want fields. Before you look at
`003-structured-output`, try to make this project return typed Java objects.
The [structured output documentation](https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html)
covers everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`003-structured-output` sub-module.

---

## The Challenge

### Task 1 - Design the domain

Create a `Match` record (stage, fixture, venue, key players, things to watch), a `Matches`
wrapper record, and a `Venue` record (name, city, country, capacity). Annotate every field with
`@JsonPropertyDescription` - e.g. the fixture field described as `"The fixture as 'Home vs Away',
e.g., 'Morocco vs Spain'"`.

Think about: where do those descriptions end up? Call an endpoint and read the
`SimpleLoggerAdvisor` output before and after adding them.

### Task 2 - Return an entity

Change a copy of `/matches` (the `getMatches` endpoint) to return `Matches` instead of
`String`. One line should change in the call chain.

### Task 3 - The four retrieval variants

`entity(Class)` (Task 2) is one of four ways to get a typed result. Build the other three as their own endpoints, each asking a genuinely different
question rather than the same one four times over - a caller should be able to tell what shape to expect from the route name alone, without a flag
that silently changes the response contract:

* `entity(ParameterizedTypeReference)` - the generic form is for collections: a venue lookup returning `List<Venue>`, a target type a class literal
  cannot express
* `responseEntity(Class)` - a single entity plus what `entity()` throws away (model, token usage, finish reason): try an endpoint where the model
  *picks* the standout match of a tournament stage
* `responseEntity(ParameterizedTypeReference)` - metadata and a collection together: a team's fixtures for a given stage

### Task 4 - Order matters too

Design a `TeamStanding` record (team, played, won, drawn, lost, goal difference, points) and an endpoint returning `List<TeamStanding>` for a group,
ranked by points then goal difference. Shape validation alone (Task 6) does not tell you whether the list came back in the right order. Think about
what would.

### Task 5 - Native structured output

Ask Gemini itself to constrain generation to your schema instead of appending instructions to the prompt (look up `EntityParamSpec`), on your venue
endpoint from Task 3. Compare the
`SimpleLoggerAdvisor` output of both approaches - what disappeared from the prompt? Then try switching it on for a whole client instead of per call,
and reuse that client for your Task 4 endpoint with a custom output converter. Does anything about the prompt look wrong now?

### Task 6 - Schema validation

Point a structured, single-match endpoint at the flash-lite model (e.g. a full preview of an already-named fixture like `Türkiye vs Australia`) and
enable schema validation with retries. How many attempts does Spring AI make before giving up?

---

## Helping Material

* [Spring AI - Structured Output Converter](https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## Done?

Open [003-structured-output](../003-structured-output) and compare. Then look at
`converter/WorldCup2026BeanOutputConverter` - it works around a real Spring AI bug you may have hit in Task 5, where a client-wide native structured
output default collides with a converter that still appends its own format instructions.
