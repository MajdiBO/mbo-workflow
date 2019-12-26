package io.mbo.labs.workflow

import io.mbo.labs.statemachine.Transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BaseWorkflowTest {

    @Test @Suppress("UNCHECKED_CAST")
    fun `check auto trigger`() {
        val baseWorkflow = BaseWorkflow(
            StartingStep("start"),
            listOf(
                Transition(StartingStep("start"), StartingStep("2"), StepDecision.NEXT),
                Transition(StartingStep("2"), StartingStep("3"), StepDecision.NEXT),
                Transition(StartingStep("3"), AutoFinalStep("final"), StepDecision.NEXT)
            ) as List<Transition<Task, StepDecision>>,
             TriggerType.AUTO
        )

        assertEquals(StepDecision.STARTED, baseWorkflow.stepDecision)
        baseWorkflow.execute(WorkflowContext())
        assertEquals(StepDecision.NEXT, baseWorkflow.stepDecision)

    }

    @Test
    fun `check manual trigger`() {

        val baseWorkflow = BaseWorkflow(
            StartingStep("start"),
            listOf(
                Transition(StartingStep("start"), ManualFinalStep("final"), StepDecision.NEXT)
            ),
            TriggerType.MANUAL
        )

        assertEquals(StepDecision.STARTED, baseWorkflow.stepDecision)
        baseWorkflow.execute(WorkflowContext())
        assertEquals(StepDecision.IN_PROGRESS, baseWorkflow.stepDecision)

        baseWorkflow.execute(WorkflowContext())
        assertEquals(StepDecision.NEXT, baseWorkflow.stepDecision)
    }

    @Test
    fun `compose nested workflow`() {

        val childWorkflow = BaseWorkflow(
            StartingStep("start"),
            listOf(
                Transition(StartingStep("start"), ManualFinalStep("final"), StepDecision.NEXT)
            ),
            TriggerType.AUTO,
            true
        )

        val composedWorkflow = BaseWorkflow(
            StartingStep("start"),
            listOf(
                Transition(StartingStep("start"), childWorkflow, StepDecision.NEXT)
            ),
            TriggerType.AUTO
        )

        composedWorkflow.execute(WorkflowContext())
        assertEquals(StepDecision.IN_PROGRESS, composedWorkflow.stepDecision)

        composedWorkflow.execute(WorkflowContext())
        assertEquals(StepDecision.NEXT, composedWorkflow.stepDecision)


    }


}

data class StartingStep(override val name:String) : Task(name, TriggerType.AUTO) {
    override fun execute(context: WorkflowContext): StepDecision {
        println("execute $name in $triggerType mode")
        return StepDecision.NEXT
    }
}

data class ManualFinalStep(override val name:String) : Task(name, TriggerType.MANUAL, true) {
    override fun execute(context: WorkflowContext): StepDecision {
        println("execute $name in $triggerType mode")
        return StepDecision.NEXT
    }
}

data class AutoFinalStep(override val name:String) : Task(name, TriggerType.AUTO, true) {
    override fun execute(context: WorkflowContext): StepDecision {
        println("execute $name in $triggerType mode")
        return StepDecision.NEXT
    }
}
