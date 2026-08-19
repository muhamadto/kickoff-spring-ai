# Exercise: Build It Yourself Before Opening [015-observability](../015-observability)

By now `/chat` is the most complex pipeline in the series: a classification call, then either a fixed refusal, a booking-intent call plus structured
extraction and deterministic validation, or a step-back rewrite plus two independent retrievals and generation, plus a Postgres history write, all
behind one request. When something's slow or wrong, log lines from each of those pieces are scattered and hard to correlate. Before you look at
`015-observability`, try to make one request's whole journey visible yourself.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`015-observability` sub-module.

---

## The Challenge

### Task 1 - Turn on what's already there

Spring AI instruments `ChatClient` calls, advisor execution and vector store operations via Micrometer automatically, with no Spring AI-specific code
required, as soon as Micrometer and a tracer are on the classpath. Add `spring-boot-starter-actuator`, a Micrometer tracing bridge, and a tracer
exporter of your choice (Zipkin is a reasonable default; a Zipkin container is one line in
`docker-compose.yml`).

Think about: if the instrumentation is automatic, what exactly is left for you to configure versus what Spring AI already does for you?

### Task 2 - Trace three different request shapes

Send a general question, a booking request, and a message that gets refused by
`MessageClassificationService`, and look at the resulting trace for each in your tracing backend. Write down how many spans each one produces, and
which components show up in each.

Think about: why should a classification refusal produce the shortest trace of the three? What does a booking request's trace tell you about which
retrieval paths never ran for it?

### Task 3 - Decide what never belongs in a trace

Spring AI's observation properties for logging prompts, completions and retrieved query content default to disabled. Leave them that way, and write
down why, specifically for this app, turning any of them on would be a bigger risk than a database row holding the same content.

Think about: what's different about the blast radius of a trace or a log line versus a database row that only your own code queries?

### Task 4 - Expose metrics too, not just traces

Add a Prometheus registry and expose `/actuator/prometheus`. Find the counters and histograms for the model calls and vector store operations you
already saw as spans in Task 2.

Think about: when would you reach for a trace versus a metric, given they come from the same underlying observations?

---

## Helping Material

* [Spring AI - Observability](https://docs.spring.io/spring-ai/reference/observability/index.html)
* [OpenTelemetry — Semantic Conventions for Generative AI (overview)](https://opentelemetry.io/docs/specs/semconv/gen-ai/)
* [Micrometer — Naming Meters](https://docs.micrometer.io/micrometer/reference/concepts/naming.html)
* [Spring Boot - actuator](https://docs.spring.io/spring-boot/reference/actuator/metrics.html)
* [Spring Boot - tracing](https://docs.spring.io/spring-boot/reference/actuator/tracing.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)
---

## Done?

Open [015-observability](../015-observability) and compare. Pay attention to how little Java changed compared to how much visibility was gained, and
to the commented-out, deliberately-disabled
`log-prompt`/`log-completion`/`log-query-response` properties in `application.yaml`.
