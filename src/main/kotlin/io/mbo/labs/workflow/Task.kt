package io.mbo.labs.workflow

import io.mbo.labs.statemachine.State
import java.util.*

abstract class Task(
    open val name: String = "task-${UUID.randomUUID()}",
    val triggerType: TriggerType,
    val final: Boolean = false
) : State {
    var stepDecision = StepDecision.STARTED

    open fun isApplicable(context: WorkflowContext): Boolean {
        return true
    }

    abstract fun execute(context: WorkflowContext): StepDecision

}