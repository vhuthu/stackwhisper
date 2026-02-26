package com.vhuthuk.core

import android.content.Context
import android.util.Log
import com.vhuthuk.core.rules.CoroutineScopeRule
import com.vhuthuk.core.rules.FragmentNotAttachedRule
import com.vhuthuk.core.rules.HiltInjectionFailureRule
import com.vhuthuk.core.rules.NetworkOnMainThreadRule
import com.vhuthuk.core.rules.NoClassDefFoundRule
import com.vhuthuk.core.rules.NullPointerRule
import com.vhuthuk.core.rules.OutOfMemoryRule
import com.vhuthuk.core.rules.ViewAfterDestroyRule
import java.util.concurrent.atomic.AtomicBoolean

object StackWhisper {

    private val rules = mutableListOf<ErrorRule>()
    private val isInitialized = AtomicBoolean(false)

    fun init(context: Context) {
        if (!isInitialized.compareAndSet(false, true)) {
            Log.w("StackWhisper", "⚠️ StackWhisper.init() called more than once. Ignoring.")
            return
        }

        rules.addAll(defaultRules())

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            val ctx = ErrorContext.from(throwable)
            val match = rules.firstOrNull { it.matches(throwable, ctx) }
            val explanation = match?.explain() ?: ErrorExplanation.unknown(throwable)
            explanation.printToLogcat(throwable)
        }

        Log.d("StackWhisper", "✅ StackWhisper v0.2 initialized — ${rules.size} rules loaded.")
    }

    fun addRule(rule: ErrorRule) {
        rules.add(rule)
    }

    private fun defaultRules(): List<ErrorRule> = listOf(
        HiltInjectionFailureRule(),
        FragmentNotAttachedRule(),
        ViewAfterDestroyRule(),
        CoroutineScopeRule(),
        NetworkOnMainThreadRule(),
        OutOfMemoryRule(),
        NoClassDefFoundRule(),
        NullPointerRule()
    )
}