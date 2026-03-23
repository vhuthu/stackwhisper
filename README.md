# StackWhisper 🔍

> Human-readable explanations for Android crashes.

Most Android crashes tell you **what** went wrong but not **why** or **how to fix it**.  
StackWhisper intercepts crashes and prints a plain-English explanation directly in Logcat.

---

## The Problem

This is what Android gives you today:

```
java.lang.RuntimeException: Unable to start activity
    Caused by: java.lang.IllegalStateException: Fragment not attached to a context
        at com.example.app.HomeFragment.loadData(HomeFragment.kt:42)
        at com.example.app.HomeFragment.onCreate(HomeFragment.kt:28)
        ... 13 more
```

You now have to Google it, read Stack Overflow, and guess.

---

## What StackWhisper Gives You

```
┌─────────────────────────────────────
│ 🔍 Fragment Not Attached To Context
│ Confidence: 85%
│
│ Cause:  You are trying to access context or activity inside a Fragment
│         before it is attached, or after it has been detached.
│
│ Fix:    Move your context access into onViewCreated() or later.
│         Never call requireContext() in the constructor or before
│         onAttach() has been called.
└─────────────────────────────────────
```

No Googling. No guessing. Just a clear explanation and a fix.

---

## Supported Crash Types

| Rule | Exception | Confidence |
|---|---|---|
| Fragment Not Attached | `IllegalStateException` | 85% |
| Network On Main Thread | `NetworkOnMainThreadException` | 99% |
| Null Pointer | `NullPointerException` | 75% |
| Coroutine Wrong Scope | `IllegalStateException` | 72% |
| View After Destroy | `IllegalStateException` | 85% |

---

## Installation

Add the dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
     debugImplementation("io.github.vhuthu-core:stackwhisper:0.1.0")
}
```

> ⚠️ Use `debugImplementation` — StackWhisper is a development tool,
> not meant for production builds.

---

## Initialize

In your `Application` class or `MainActivity`:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StackWhisper.init(this)
    }
}
```

That's it. StackWhisper will now intercept crashes automatically
and print explanations to Logcat filtered by `StackWhisper`.

---

## How It Works

StackWhisper hooks into Android's `UncaughtExceptionHandler`.
When a crash occurs it walks the full cause chain, matches it
against a set of rules, and prints a structured explanation.
It then re-throws the exception so your app still crashes normally —
StackWhisper never swallows errors.

---

## Contributing

Contributions are welcome!

If you've seen a confusing crash:

Add a rule,
Improve explanations,
Open a PR

---

## License

```
Copyright 2026 StackWhisper Contributors

Licensed under the Apache License, Version 2.0
```

---

> Built by Vhuthu Kwinda - Android developer tired of cryptic stacktraces. 🤝
