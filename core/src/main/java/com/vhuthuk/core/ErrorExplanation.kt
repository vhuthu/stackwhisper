package com.vhuthuk.core

import android.util.Log

data class ErrorExplanation(
    val title: String,
    val probableCause: String,
    val suggestedFix: String,
    val confidence: Int
) {
    fun printToLogcat() {
        Log.e("StackWhisper", """
            ┌─────────────────────────────────────
            │ 🔍 $title
            │ Confidence: $confidence%
            │
            │ Cause:  $probableCause
            │
            │ Fix:    $suggestedFix
            └─────────────────────────────────────
        """.trimIndent())
    }

    companion object {
        fun unknown(t: Throwable) = ErrorExplanation(
            title = "Unrecognized crash: ${t::class.simpleName}",
            probableCause = "No matching rule found.",
            suggestedFix = "Check the full stacktrace above.",
            confidence = 0
        )
    }
}