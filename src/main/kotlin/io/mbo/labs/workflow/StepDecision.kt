package io.mbo.labs.workflow

import io.mbo.labs.statemachine.Event

enum class StepDecision : Event {
    STARTED, IN_PROGRESS, NEXT, SKIPPED, FAILED, CANCELED
}