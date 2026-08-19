# Exercise: Build It Yourself Before Opening [004-advisors](../004-advisors)

You already have a `SimpleLoggerAdvisor` wired into every client. That advisor is one example of a general mechanism - advisors intercept, modify, and
enrich the request before it reaches the model. Before you look at `004-advisors`, try to build your own.
The [advisors documentation](https://docs.spring.io/spring-ai/reference/api/advisors.html)
covers everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`004-advisors` sub-module.

---

## The Challenge

### Task 1 - Mutate the user message

A fan might paste an email address or phone number into any prompt, e.g. `/players?name=remind me
at fan@example.com about Kylian Mbappé`, or the same trick on `/news`'s `team` parameter. Write a
`PiiRedactionAdvisor` that implements `CallAdvisor`, scans each user message for email addresses and phone numbers, and replaces them with
`[EMAIL_REDACTED]` / `[PHONE_REDACTED]` before the prompt reaches the model. Register it as a
`defaultAdvisor` on every client.

Think about: why is this an advisor and not a prompt template? What would the controller have to remember if redaction lived in the prompt?

### Task 2 - Prove the model never saw the PII

Give your `PiiRedactionAdvisor` an order that runs *before* the `SimpleLoggerAdvisor`. Call an endpoint with an email in the parameter. Read the log -
does the `SimpleLoggerAdvisor` print the redacted prompt or the original? Now swap the order so redaction runs *after* logging. What changes in the
log, and did the model still receive the PII?

Think about: why does order matter when one advisor inspects what another mutated?

---

## Helping Material

* [Spring AI - brief description of Advisors](https://docs.spring.io/spring-ai/reference/api/chatclient.html#_advisors)
* [Spring AI - complete guide of Advisors API](https://docs.spring.io/spring-ai/reference/api/advisors.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## Done?

Open [004-advisors](../004-advisors) and compare. Pay attention to how `PiiRedactionAdvisor` is registered as a `defaultAdvisor` in `GenAiConfig`, and
to how its `order` (10) runs before the
`SimpleLoggerAdvisor` (30) so the log proves the model never saw the PII.