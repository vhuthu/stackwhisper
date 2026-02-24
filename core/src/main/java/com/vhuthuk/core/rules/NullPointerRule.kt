package com.vhuthuk.core.rules

import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule

class NullPointerRule : ErrorRule {

    override val id = "NULL_POINTER"
    override val category = ErrorCategory.GENERAL

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        return context.causeChain.any { cause ->
            cause is NullPointerException
        }
    }

    override fun explain() = ErrorExplanation(
        title = "Null Pointer Exception",
        probableCause = "You are trying to use an object that is null. " +
                "This often happens when force unwrapping with !! on a " +
                "nullable value, or when a Java library returns null " +
                "unexpectedly.",
        suggestedFix = "Check for null before using the object. Replace " +
                "!! with ?. or ?: to handle null safely. If this comes from " +
                "a Java library, wrap the call with a null check.",
        confidence = 75
    )
}