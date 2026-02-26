package com.vhuthuk.core.rules

import com.vhuthuk.core.ErrorCategory
import com.vhuthuk.core.ErrorContext
import com.vhuthuk.core.ErrorExplanation
import com.vhuthuk.core.ErrorRule

class NoClassDefFoundRule : ErrorRule {

    override val id = "NO_CLASS_DEF_FOUND"
    override val category = ErrorCategory.DEPENDENCY_INJECTION

    override fun matches(throwable: Throwable, context: ErrorContext): Boolean {
        return context.causeChain.any { cause ->
            cause is NoClassDefFoundError ||
                    cause is ClassNotFoundException
        }
    }

    override fun explain() = ErrorExplanation(
        title = "Class Not Found At Runtime",
        probableCause = "A class that your code depends on could not be " +
                "found at runtime. This commonly happens in multi-module projects " +
                "when a module uses 'implementation' instead of 'api' in its " +
                "build.gradle.kts, hiding transitive dependencies from other modules.",
        suggestedFix = "Check the following: (1) In your module's " +
                "build.gradle.kts, change 'implementation' to 'api' for any " +
                "dependency that other modules need to access directly. " +
                "(2) Make sure the dependency is not missing entirely from " +
                "your dependencies block. (3) Clean and rebuild your project " +
                "via Build → Clean Project → Rebuild Project.",
        confidence = 79,
        category = ErrorCategory.DEPENDENCY_INJECTION
    )
}