package com.vhuthuk.core.rules

import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule


class FragmentNotAttachedRule : ErrorRule {

    override val id = "FRAGMENT_NOT_ATTACHED"
    override val category = ErrorCategory.LIFECYCLE

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        return context.causeChain.any { cause ->
            cause is IllegalStateException &&
                    (context.stackContains("not attached to a context") ||
                            context.stackContains("not attached to an activity"))
        }
    }

    override fun explain() = ErrorExplanation(
        title = "Fragment Not Attached To Context",
        probableCause = "You are trying to access context or activity " +
                "inside a Fragment before it is attached, or after it has " +
                "been detached.",
        suggestedFix = "Move your context access into onViewCreated() " +
                "or later. Never call requireContext() in the constructor " +
                "or before onAttach() has been called.",
        confidence = 85
    )
}