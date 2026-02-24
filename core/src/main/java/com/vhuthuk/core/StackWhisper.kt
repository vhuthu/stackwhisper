package com.vhuthuk.core

import android.content.Context
import com.vhuthuk.core.rules.CoroutineScopeRule
import com.vhuthuk.core.rules.FragmentNotAttachedRule
import com.vhuthuk.core.rules.NetworkOnMainThreadRule
import com.vhuthuk.core.rules.NullPointerRule
import com.vhuthuk.core.rules.ViewAfterDestroyRule

object StackWhisper {

    private val rules = mutableListOf<ErrorRule>()

    fun init(context: Context) {
        rules.addAll(defaultRules())

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            val ctx = ErrorContext.from(throwable)
            val match = rules.firstOrNull { it.matches(throwable, ctx) }
            val explanation = match?.explain() ?: ErrorExplanation.unknown(throwable)
            explanation.printToLogcat()
        }
    }

    fun addRule(rule: ErrorRule) {
        rules.add(rule)
    }

    private fun defaultRules(): List<ErrorRule> = listOf(
        FragmentNotAttachedRule(),
        NetworkOnMainThreadRule(),
        NullPointerRule(),
        CoroutineScopeRule(),
        ViewAfterDestroyRule()
    )
}