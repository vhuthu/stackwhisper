package com.vhuthuk.core.rules


import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule

class CoroutineScopeRule : ErrorRule {

    override val id = "COROUTINE_WRONG_SCOPE"
    override val category = ErrorCategory.COROUTINES

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        return context.causeChain.any { cause ->
            cause is IllegalStateException &&
                    (context.stackContains("GlobalScope") ||
                            context.stackContains("JobCancellationException") ||
                            context.stackContains("StandaloneCoroutine") ||
                            context.stackContains("coroutine was cancelled"))
        }
    }

    override fun explain() = ErrorExplanation(
        title = "Coroutine Launched in Wrong Scope",
        probableCause = "A coroutine was launched using GlobalScope or a " +
                "scope that is not tied to the Android lifecycle. This causes " +
                "coroutines to keep running after the screen is destroyed, " +
                "leading to memory leaks or crashes.",
        suggestedFix = "Replace GlobalScope.launch with viewModelScope.launch " +
                "inside a ViewModel, or lifecycleScope.launch inside an Activity " +
                "or Fragment. These scopes cancel automatically when the " +
                "lifecycle ends.",
        confidence = 72,
        category = category
    )
}