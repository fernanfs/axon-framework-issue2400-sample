# About this project

This project serves as an example for [Issue 2400](https://github.com/AxonFramework/AxonFramework/issues/2400).

When validating events using the `AggregateTestFixture`, there is the option to perform the validation using Hamcrest `Matcher`s. Unfortunately, the output of a failed validation is extremely unreadable, as the information about the non-match is basically skipped.

Given the example in `de.digitalfrontiers.axon.issue2400.ExampleAggregateTest`, the validation using the `expectEventsMatching` will always return the same error message, independent of the actual mismatch. This message looks like this:

```text
The published events do not match the expected events.Expected :
list with exact sequence of: <Message with payload <(is an instance of de.digitalfrontiers.axon.issue2400.ValuesReceivedEvent and hasProperty("intValue", <42>) and hasProperty("stringValue", "Arthur Dent") and hasProperty("boolValue", <false>) and hasProperty("longValue", <4712>))>> (FAILED!)
But got:
GenericDomainEventMessage: GenericDomainEventMessage{payload={ValuesReceivedEvent(id=example, intValue=42, stringValue=Arthur Dent, boolValue=false, longValue=4711)}, metadata={}, messageIdentifier='d3ea69b7-f621-4b94-a5ac-fb602a9bd512', timestamp='2022-09-28T10:29:11.116255Z', aggregateType='ExampleAggregate', aggregateIdentifier='example', sequenceNumber=1}
org.axonframework.test.AxonAssertionError: The published events do not match the expected events.Expected :
list with exact sequence of: <Message with payload <(is an instance of de.digitalfrontiers.axon.issue2400.ValuesReceivedEvent and hasProperty("intValue", <42>) and hasProperty("stringValue", "Arthur Dent") and hasProperty("boolValue", <false>) and hasProperty("longValue", <4712>))>> (FAILED!)
But got:
GenericDomainEventMessage: GenericDomainEventMessage{payload={ValuesReceivedEvent(id=example, intValue=42, stringValue=Arthur Dent, boolValue=false, longValue=4711)}, metadata={}, messageIdentifier='d3ea69b7-f621-4b94-a5ac-fb602a9bd512', timestamp='2022-09-28T10:29:11.116255Z', aggregateType='ExampleAggregate', aggregateIdentifier='example', sequenceNumber=1}
	at app//de.digitalfrontiers.axon.issue2400.ExampleAggregateTest.perform validation within the fixture using hamcrest$axon_framework_issue2400_sample(ExampleAggregateTest.kt:54)
	at java.base@17.0.2/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base@17.0.2/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
```

This is due to the fact, that Axon doesn't use Hamcrest `Matcher.describeMismatch()` to actually describe the mismatch.

In contrast, using the same payload matcher, but invoking `Matcher.describeMismatch()` will yield a result like this:

```text
hasProperty("longValue", <4712>)  property 'longValue' was <4711L>
```
