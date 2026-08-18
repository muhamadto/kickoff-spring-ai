# Exercise: Break It Before Opening [013-guarded-rag](../013-guarded-rag)

`/chat` now decides whether to book a ticket inside the model's own reasoning loop: `BookingTool`
is a tool the model calls when it judges a fan's message is an actual booking request, with no validation once it decides to call it. Retrieval is
back to plain `QuestionAnswerAdvisor`, dropping
`011-hybrid-search-rag`'s hand-rolled fusion, but still a fixed advisor either way. Before you look at `013-guarded-rag`, try to find and understand
`BookingTool`'s failure modes yourself. The
[Tool Calling](https://docs.spring.io/spring-ai/reference/api/tools.html) and
[Building Effective Agents](https://docs.spring.io/spring-ai/reference/api/effective-agents.html)
documentation cover the mechanisms below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`013-guarded-rag` sub-module.

---

## The Challenge

### Task 1 - Make it book when nobody asked

Ask about a match in a way that mentions wanting to go, without actually asking to book ("I'd love to be at the Morocco game", "Wish I could see that
one live"). Watch `model.tool: DEBUG`: does
`bookMatchTicket` fire anyway?

Think about: what part of the tool's description would you sharpen to fix this, and is a better description alone ever a complete fix for a decision
the model gets to make?

### Task 2 - Make it skip a booking it should have made

Ask clearly for a booking, but phrase the fixture ambiguously (a team nickname, a date described relatively, "the one this weekend"). Does the model
still call the tool, guessing at the missing details, or does it decline to book at all?

Think about: is guessing at missing details actually better or worse than not booking? Who bears the cost if it's wrong either way?

### Task 3 - Book something that doesn't exist

Ask it to book tickets for a fixture that was never played, or teams that never met. Does
`bookMatchTicket` still return a confident booking reference?

Think about: could the tool itself refuse this, or does that decision belong somewhere that runs before the model gets involved at all?

### Task 4 - Book the same thing twice

Ask for the same booking twice in one conversation. Does anything notice, or do you end up with two booking references for what should be one booking?

Think about: `bookMatchTicket` has no memory of what it's already booked. What would it need to check that against?

### Task 5 - Sketch the fix, in your own words, before you look

For each rough edge above, is the fix something you'd trust the model to improve at with a better prompt or tool description, or something you'd
rather enforce in code that runs whether or not the model cooperates? Write down which is which before opening `013-guarded-rag`.

---

## Done?

Open [013-guarded-rag](../013-guarded-rag) and compare. Pay attention to how its deterministic gates map to the rough edges above, and which of your
fixes it agrees with.
