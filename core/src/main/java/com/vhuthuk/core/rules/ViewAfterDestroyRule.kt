package com.vhuthuk.core.rules

import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule

class ViewAfterDestroyRule : ErrorRule {

    override val id = "VIEW_AFTER_DESTROY"
    override val category = ErrorCategory.LIFECYCLE

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        return context.causeChain.any { cause ->
            cause is IllegalStateException &&
                    (context.stackContains("Fragment view has been destroyed") ||
                            context.stackContains("onDestroyView") ||
                            context.stackContains("getViewLifecycleOwner"))
        }
    }

    override fun explain() = ErrorExplanation(
        title = "View Accessed After onDestroyView",
        probableCause = "You are trying to access a Fragment's view after " +
                "it has already been destroyed. This commonly happens when a " +
                "coroutine or callback finishes after the Fragment is gone.",
        suggestedFix = "Use viewLifecycleOwner.lifecycleScope.launch instead " +
                "of lifecycleScope.launch inside Fragments. This ensures your " +
                "coroutine cancels automatically when the view is destroyed.",
        confidence = 85,
        category = category
    )
}