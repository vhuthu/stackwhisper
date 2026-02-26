package com.vhuthuk.stackwhisper

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vhuthuk.core.StackWhisper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StackWhisper.init(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //This where sample testing I was doing lol

       // triggerFragmentNotAttachedCrash()
        //triggerNetworkOnMainThread()
        //triggerNullPointer()
        //triggerCoroutineScopeError()
        //triggerViewAfterDestroy()
        //triggerOutOfMemory()
        //triggerHiltInjectionFailure()
    }

    private fun triggerFragmentNotAttachedCrash(){
        throw IllegalStateException("Fragment not attached to a context")
    }

    private fun triggerNetworkOnMainThread() {
        java.net.URL("https://google.com").readText()
    }

    private fun triggerNullPointer() {
        val name: String? = null
        println(name!!.length)
    }

    private fun triggerCoroutineScopeError() {
        val job = GlobalScope.launch {
            // simulate some work
        }
        job.cancel()
        throw IllegalStateException("coroutine was cancelled")
    }

    private fun triggerViewAfterDestroy() {
        throw IllegalStateException("Fragment view has been destroyed")
    }

    private fun triggerOutOfMemory() {
        val list = mutableListOf<ByteArray>()
        while (true) {
            list.add(ByteArray(1024 * 1024)) // allocate 1MB at a time
        }
    }

    private fun triggerHiltInjectionFailure() {
        val cause = NullPointerException("injected field is null")
        val stack = Thread.currentThread().stackTrace
        val fakeElement = StackTraceElement(
            "com.example.app.Hilt_MainActivity",
            "inject",
            "Hilt_MainActivity.java",
            42
        )
        cause.stackTrace = arrayOf(fakeElement) + stack
        throw RuntimeException("Unable to start activity", cause)
    }
}