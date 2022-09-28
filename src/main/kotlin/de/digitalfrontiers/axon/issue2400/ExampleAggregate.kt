package de.digitalfrontiers.axon.issue2400

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ExampleAggregate {

  @AggregateIdentifier
  private lateinit var id: String

  constructor()

  @CommandHandler
  constructor(cmd: CreateExample) {
    AggregateLifecycle.apply(ExampleCreatedEvent(cmd.id))
  }

  @EventSourcingHandler
  fun on(e: ExampleCreatedEvent) {
    id = e.id
  }
  
  @CommandHandler
  fun handle(cmd: CommandWithValues) {
    AggregateLifecycle.apply(
      ValuesReceivedEvent(
        id = cmd.id,
        intValue = cmd.intValue,
        stringValue = cmd.stringValue,
        boolValue = cmd.boolValue,
        longValue = cmd.longValue
      )
    )
  }
}


data class CreateExample(
  @TargetAggregateIdentifier
  val id: String
)

data class ExampleCreatedEvent(val id: String)

data class CommandWithValues(
  @TargetAggregateIdentifier
  val id: String,
  val intValue: Int,
  val stringValue: String,
  val boolValue: Boolean,
  val longValue: Long
)

data class ValuesReceivedEvent(
  @TargetAggregateIdentifier
  val id: String,
  val intValue: Int,
  val stringValue: String,
  val boolValue: Boolean,
  val longValue: Long
)
