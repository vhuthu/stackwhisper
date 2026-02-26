package com.vhuthuk.core.rules

import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule

class OutOfMemoryRule : ErrorRule {

    override val id = "OUT_OF_MEMORY"
    override val category = ErrorCategory.GENERAL

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        return context.causeChain.any { cause ->
            cause is OutOfMemoryError
        }
    }

    override fun explain(): ErrorExplanation {
        val runtime = Runtime.getRuntime()
        val usedMb = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L
        val maxMb = runtime.maxMemory() / 1048576L
        val freeMb = runtime.freeMemory() / 1048576L

        return ErrorExplanation(
            title = "Out Of Memory Error",
            probableCause = "Your app ran out of available heap memory. " +
                    "At the time of crash — Used: ${usedMb}MB, " +
                    "Free: ${freeMb}MB, Max allowed: ${maxMb}MB.",
            suggestedFix = "Check for memory leaks using Android Studio " +
                    "Memory Profiler. Common causes are: loading large bitmaps " +
                    "without sampling, holding references to Activities or " +
                    "Fragments longer than needed, or accumulating objects " +
                    "in a list without clearing them.",
            confidence = 95,
            category = category
        )
    }
}