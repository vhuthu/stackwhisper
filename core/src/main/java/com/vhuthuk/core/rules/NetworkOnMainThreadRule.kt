package com.vhuthuk.core.rules

import android.os.NetworkOnMainThreadException
import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule

class NetworkOnMainThreadRule : ErrorRule {

    override val id = "NETWORK_ON_MAIN_THREAD"
    override val category = ErrorCategory.COROUTINES

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        return context.causeChain.any { cause ->
            cause is NetworkOnMainThreadException
        }
    }

    override fun explain() = ErrorExplanation(
        title = "Network Call on Main Thread",
        probableCause = "You are making a network request directly on " +
                "the main thread. Android does not allow this because it " +
                "would freeze your UI.",
        suggestedFix = "Wrap your network call inside a coroutine using " +
                "withContext(Dispatchers.IO) { } or move it into a " +
                "ViewModel using viewModelScope.launch { }.",
        confidence = 99
    )
}