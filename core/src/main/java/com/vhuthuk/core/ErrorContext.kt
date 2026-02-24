package com.vhuthuk.core

data class ErrorContext(
    val throwable: Throwable,
    val threadName: String,
    val stackTraceText: String,
    val causeChain: List<Throwable>
) {
    fun stackContains(text: String) =
        stackTraceText.contains(text, ignoreCase = true)

    companion object {
        fun from(t: Throwable): ErrorContext {
            val causes = buildList {
                var current: Throwable? = t
                while (current != null) {
                    add(current)
                    current = current.cause
                }
            }
            return ErrorContext(
                throwable = t,
                threadName = Thread.currentThread().name,
                stackTraceText = t.stackTraceToString(),
                causeChain = causes
            )
        }
    }
}