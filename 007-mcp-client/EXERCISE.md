# Exercise: Build It Yourself Before Opening [008-chat-memory](../008-chat-memory)

Every endpoint so far is stateless. The model has no memory of what the fan asked in the previous call. Before you look at `008-chat-memory`, try to
give it one. The [chat memory documentation](https://docs.spring.io/spring-ai/reference/api/chat-memory.html)
covers everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`008-chat-memory` sub-module.

---

## The Challenge

### Task 1 - Give the model a memory

Wire a `MessageChatMemoryAdvisor` onto a new `ChatClient` built by mutating the flash client, so memory only applies to the endpoints that need it.
Use `MessageWindowChatMemory` with the auto-configured `ChatMemoryRepository` and a window of 20 messages.

Think about: why mutate the flash client instead of adding the advisor to the existing
`geminiFlashChatClient`? What happens to the other endpoints if you add it there?

### Task 2 - The conversation ID

The `ChatMemory.CONVERSATION_ID` parameter is required on every call that uses the memory client. Add an endpoint that takes a `conversationId`
parameter and passes it to the advisor context. Call it without the parameter and read the error. What does Spring AI throw?

Think about: why is there no default conversation ID? What would go wrong if there was one?

### Task 3 - Store and retrieve

Add two endpoints: `/fan-intro?conversationId=...&team=...` to store the fan's team in memory, and
`/fan-recap?conversationId=...` to ask the model what team the fan supports. Call intro first, then recap with the same conversation ID. Does the
model remember?

Now call recap with a different conversation ID. Does it remember? Why not?

### Task 4 - Persistent memory

The default `ChatMemoryRepository` is in-memory. It is not production ready and is local to the instance, so memory would not be shared between
autoscaled instances. Restart the application and call `/fan-recap` with the same conversation ID. Is the memory still there? Now switch to a
JDBC-backed
`ChatMemoryRepository` (add the `spring-ai-starter-model-chat-memory-repository-jdbc`
dependency and configure a Postgres datasource in the yaml). Restart and call recap again. What changed?

Think about: why does in-memory memory break when you scale to two instances behind a load balancer? What does a shared, JDBC-backed repository give
you that in-memory does not?

### Task 5 - Window eviction

Set `maxMessages` to a small number (e.g. 3) and call `/fan-intro` then `/fan-recap` several times with the same conversation ID. At what point does
the model forget the team? How many turns does it take for the intro message to be evicted from the window?

Think about: what is a turn? Why does `MessageWindowChatMemory` evict whole turns rather than individual messages?

---

## Helping Material

* [Spring AI brief description of chat memory](https://docs.spring.io/spring-ai/reference/api/chatclient.html#_chat_memory)
* [Spring AI complete guide of Chat Memory](https://docs.spring.io/spring-ai/reference/api/chat-memory.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## Done?

Open [008-chat-memory](../008-chat-memory) and compare. Pay attention to how
`geminiMemoryAwareChatClient` is built by mutating the flash client, and how
`MessageChatMemoryAdvisor` is wired with `ChatMemoryRepository` so memory survives restarts.