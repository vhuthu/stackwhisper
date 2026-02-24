package com.vhuthuk.core

import com.vhuthuk.core.rules.CoroutineScopeRule
import com.vhuthuk.core.rules.FragmentNotAttachedRule
import com.vhuthuk.core.rules.NetworkOnMainThreadRule
import com.vhuthuk.core.rules.NullPointerRule
import com.vhuthuk.core.rules.ViewAfterDestroyRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RulesTest {

    @Test
    fun `FragmentNotAttachedRule matches correct exception`() {
        val rule = FragmentNotAttachedRule()
        val throwable = IllegalStateException("Fragment not attached to a context")
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }

    @Test
    fun `FragmentNotAttachedRule does not match unrelated exception`() {
        val rule = FragmentNotAttachedRule()
        val throwable = IllegalStateException("Something else went wrong")
        val context = ErrorContext.from(throwable)
        assertFalse(rule.matches(throwable, context))
    }

    @Test
    fun `NetworkOnMainThreadRule matches correct exception`() {
        val rule = NetworkOnMainThreadRule()
        val throwable = android.os.NetworkOnMainThreadException()
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }

    @Test
    fun `NetworkOnMainThreadRule does not match unrelated exception`() {
        val rule = NetworkOnMainThreadRule()
        val throwable = IllegalStateException("Something else")
        val context = ErrorContext.from(throwable)
        assertFalse(rule.matches(throwable, context))
    }

    @Test
    fun `NullPointerRule matches correct exception`() {
        val rule = NullPointerRule()
        val throwable = NullPointerException("null reference")
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }

    @Test
    fun `NullPointerRule does not match unrelated exception`() {
        val rule = NullPointerRule()
        val throwable = IllegalStateException("Something else")
        val context = ErrorContext.from(throwable)
        assertFalse(rule.matches(throwable, context))
    }

    @Test
    fun `CoroutineScopeRule matches correct exception`() {
        val rule = CoroutineScopeRule()
        val throwable = IllegalStateException("coroutine was cancelled")
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }

    @Test
    fun `CoroutineScopeRule does not match unrelated exception`() {
        val rule = CoroutineScopeRule()
        val throwable = IllegalStateException("Something else")
        val context = ErrorContext.from(throwable)
        assertFalse(rule.matches(throwable, context))
    }

    @Test
    fun `ViewAfterDestroyRule matches correct exception`() {
        val rule = ViewAfterDestroyRule()
        val throwable = IllegalStateException("Fragment view has been destroyed")
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }

    @Test
    fun `ViewAfterDestroyRule does not match unrelated exception`() {
        val rule = ViewAfterDestroyRule()
        val throwable = IllegalStateException("Something else")
        val context = ErrorContext.from(throwable)
        assertFalse(rule.matches(throwable, context))
    }
}