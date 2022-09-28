package de.digitalfrontiers.axon.issue2400

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.matchers.Matchers.exactSequenceOf
import org.axonframework.test.matchers.Matchers.messageWithPayload
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.StringDescription
import org.hamcrest.beans.HasPropertyWithValue.hasProperty
import org.junit.jupiter.api.Test

internal class ExampleAggregateTest {

  @Test
  internal fun `perform validation using hamcrest`() {

    val e = ValuesReceivedEvent(
      id = "example",
      intValue = 42,
      stringValue = "Arthur Dent",
      boolValue = false,
      longValue = 4711
    )

    val matcher = eventMatcher()

    assertThat(matcher.matches(e)).isFalse

    val description = StringDescription()
    matcher.describeMismatch(e, description)

    println(description)

  }


  @Test
  internal fun `perform validation within the fixture using hamcrest`() {

    AggregateTestFixture(ExampleAggregate::class.java)
      .given(ExampleCreatedEvent("example"))
      .`when`(
        CommandWithValues(
          id = "example",
          intValue = 42,
          stringValue = "Arthur Dent",
          boolValue = false,
          longValue = 4711
        )
      )
      .expectEventsMatching(
        exactSequenceOf(
          messageWithPayload(
            eventMatcher()
          )
        )
      )

  }

  private fun eventMatcher(): Matcher<ValuesReceivedEvent> =
    allOf<ValuesReceivedEvent>(
      isA(ValuesReceivedEvent::class.java),
      hasProperty("intValue", equalTo(42)),
      hasProperty("stringValue", equalTo("Arthur Dent")),
      hasProperty("boolValue", equalTo(false)),
      // this is the value failing
      hasProperty("longValue", equalTo(4712))
    )
}
