package com.vhuthuk.core.rules

import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule

class HiltInjectionFailureRule : ErrorRule {

    override val id = "HILT_INJECTION_FAILURE"
    override val category = ErrorCategory.DEPENDENCY_INJECTION

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        val hasHiltInStack = (context.stackContains("MembersInjector") ||
                context.stackContains("_Factory") ||
                context.stackContains("dagger.internal") ||
                context.stackContains("DaggerAppComponent") ||
                context.stackContains("Hilt_") ||
                context.stackContains("HiltWrapper"))

        val hasMatchingException = context.causeChain.any { cause ->
            cause is NullPointerException || cause is IllegalStateException
        }

        return hasHiltInStack && hasMatchingException
    }

    override fun explain() = ErrorExplanation(
        title = "Hilt / Dagger Injection Failure",
        probableCause = "A dependency injection failure occurred. Either a " +
                "dependency was not provided, a class is missing @AndroidEntryPoint, " +
                "or a @HiltViewModel is not using the correct constructor.",
        suggestedFix = "Check the following: (1) Your Activity or Fragment has " +
                "@AndroidEntryPoint annotation. (2) Your ViewModel has @HiltViewModel " +
                "and @Inject constructor. (3) All dependencies are provided via " +
                "@Provides or @Binds in a @Module. (4) Your Application class has " +
                "@HiltAndroidApp.",
        confidence = 82,
        category = ErrorCategory.DEPENDENCY_INJECTION
    )
}