package com.vhuthuk.core

interface ErrorRule {
    val id: String
    val category: ErrorCategory

    fun matches(throwable: Throwable, context: ErrorContext): Boolean
    fun explain(): ErrorExplanation
}