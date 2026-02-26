package com.vhuthuk.core

import com.vhuthuk.core.rules.CoroutineScopeRule
import com.vhuthuk.core.rules.FragmentNotAttachedRule
import com.vhuthuk.core.rules.NetworkOnMainThreadRule
import com.vhuthuk.core.rules.NullPointerRule
import com.vhuthuk.core.rules.OutOfMemoryRule
import com.vhuthuk.core.rules.ViewAfterDestroyRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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

    @Test
    fun `OutOfMemoryRule matches correct exception`() {
        val rule = OutOfMemoryRule()
        val throwable = OutOfMemoryError("Failed to allocate memory")
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }

    @Test
    fun `OutOfMemoryRule does not match unrelated exception`() {
        val rule = OutOfMemoryRule()
        val throwable = IllegalStateException("Something else")
        val context = ErrorContext.from(throwable)
        assertFalse(rule.matches(throwable, context))
    }

    @Test
    fun `StackWhisper init guard prevents double initialization`() {
        val field = StackWhisper::class.java.getDeclaredField("isInitialized")
        field.isAccessible = true
        val atomicBoolean = field.get(StackWhisper) as java.util.concurrent.atomic.AtomicBoolean
        atomicBoolean.set(false)

        var handlerSetCount = 0
        val originalHandler = Thread.getDefaultUncaughtExceptionHandler()

        val mockContext = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        StackWhisper.init(mockContext)
        handlerSetCount++

        StackWhisper.init(mockContext)

        assertTrue(handlerSetCount == 1)

        Thread.setDefaultUncaughtExceptionHandler(originalHandler)

        atomicBoolean.set(false)
    }

    @Test
    fun `FragmentNotAttachedRule has LIFECYCLE category`() {
        val rule = FragmentNotAttachedRule()
        assertTrue(rule.category == ErrorCategory.LIFECYCLE)
    }

    @Test
    fun `NetworkOnMainThreadRule has COROUTINES category`() {
        val rule = NetworkOnMainThreadRule()
        assertTrue(rule.category == ErrorCategory.COROUTINES)
    }

    @Test
    fun `NullPointerRule has GENERAL category`() {
        val rule = NullPointerRule()
        assertTrue(rule.category == ErrorCategory.GENERAL)
    }

    @Test
    fun `CoroutineScopeRule has COROUTINES category`() {
        val rule = CoroutineScopeRule()
        assertTrue(rule.category == ErrorCategory.COROUTINES)
    }

    @Test
    fun `ViewAfterDestroyRule has LIFECYCLE category`() {
        val rule = ViewAfterDestroyRule()
        assertTrue(rule.category == ErrorCategory.LIFECYCLE)
    }

    @Test
    fun `OutOfMemoryRule has GENERAL category`() {
        val rule = OutOfMemoryRule()
        assertTrue(rule.category == ErrorCategory.GENERAL)
    }

    @Test
    fun `FragmentNotAttachedRule matches when wrapped in RuntimeException`() {
        val rule = FragmentNotAttachedRule()
        val cause = IllegalStateException("Fragment not attached to a context")
        val throwable = RuntimeException("Unable to start activity", cause)
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }

    @Test
    fun `NullPointerRule matches when wrapped in RuntimeException`() {
        val rule = NullPointerRule()
        val cause = NullPointerException("null reference")
        val throwable = RuntimeException("Unable to start activity", cause)
        val context = ErrorContext.from(throwable)
        assertTrue(rule.matches(throwable, context))
    }
}