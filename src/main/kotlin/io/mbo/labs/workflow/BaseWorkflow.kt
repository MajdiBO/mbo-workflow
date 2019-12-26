package io.mbo.labs.workflow

import io.mbo.labs.statemachine.StateMachine
import io.mbo.labs.statemachine.Transition

open class BaseWorkflow(
    startingStep: Task,
    transitions: List<Transition<Task, StepDecision>>,
    triggerType: TriggerType,
    final: Boolean = false
) : Task(triggerType= triggerType, final = final) {

    private val stateMachine = StateMachine(startingStep, transitions)

    override fun execute(context: WorkflowContext): StepDecision {
        stepDecision = stateMachine.currentState.run {
            if (isApplicable(context)) {
                execute(context)
            } else StepDecision.SKIPPED
        }

        if (stateMachine.currentState.final) return stepDecision
        stateMachine.onEvent(stepDecision)

        stepDecision = when {
            stateMachine.currentState.triggerType == TriggerType.AUTO || stepDecision != StepDecision.NEXT -> execute(context)
            else -> StepDecision.IN_PROGRESS
        }

        return stepDecision
    }
}

