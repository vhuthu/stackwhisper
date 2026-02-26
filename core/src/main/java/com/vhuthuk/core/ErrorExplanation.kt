package com.vhuthuk.core
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ErrorExplanation(
    val title: String,
    val probableCause: String,
    val suggestedFix: String,
    val confidence: Int,
    val category: ErrorCategory = ErrorCategory.GENERAL
) {
    fun printToLogcat() {
        val tag = "StackWhisper/${category.logTag()}"
        val timestamp = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())
        val thread = Thread.currentThread().name

        Log.e(tag, """
        $BANNER
        ┌─────────────────────────────────────
        │ 🔍 $title
        │ Confidence: $confidence%
        │ Version:    StackWhisper v0.2.0
        │ Time:       $timestamp
        │ Thread:     $thread
        │
        │ Cause:  $probableCause
        │
        │ Fix:    $suggestedFix
        └─────────────────────────────────────
    """.trimIndent())
    }

    companion object {
        fun unknown(t: Throwable): ErrorExplanation {
            val timestamp = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())
            val thread = Thread.currentThread().name

            return ErrorExplanation(
                title = "Unrecognized crash: ${t::class.simpleName}",
                probableCause = "No matching rule found.",
                suggestedFix = "Check the full stacktrace above.",
                confidence = 0
            ).also {
                Log.e("StackWhisper/UNKNOWN", """
                    ┌─────────────────────────────────────
                    │ 🔍 Unrecognized crash: ${t::class.simpleName}
                    │ Confidence: 0%
                    │ Version:    StackWhisper v0.2.0
                    │ Time:       $timestamp
                    │ Thread:     $thread
                    │
                    │ Cause:  No matching rule found.
                    │
                    │ Fix:    Check the full stacktrace above.
                    └─────────────────────────────────────
                """.trimIndent())
            }
        }
    }
}

private fun ErrorCategory.logTag(): String = when (this) {
    ErrorCategory.LIFECYCLE -> "LIFECYCLE"
    ErrorCategory.DEPENDENCY_INJECTION -> "DI"
    ErrorCategory.COROUTINES -> "COROUTINES"
    ErrorCategory.COMPOSE -> "COMPOSE"
    ErrorCategory.GENERAL -> "GENERAL"
}

private val BANNER = """
* * * * * * * * * * * * * * * * * *
*   ____  _             _          *
*  / ___|| |_ __ _  ___| | __      *
*  \___ \| __/ _` |/ __| |/ /      *
*   ___) | || (_| | (__|   <       *
*  |____/ \__\__,_|\___|_|\_\      *
*  \ \      / / | |__ (_)___ _ __  *
*   \ \ /\ / /| '_ \| / __| '_ \  *
*    \ V  V / | | | | \__ \ |_) | *
*     \_/\_/  |_| |_|_|___/ .__/  *
*                          |_|     *
* * * * * * * * * * * * * * * * * *
""".trimIndent()