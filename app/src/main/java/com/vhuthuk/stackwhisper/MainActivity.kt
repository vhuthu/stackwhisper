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

        //triggerFragmentNotAttachedCrash()
        //triggerNetworkOnMainThread()
        //triggerNullPointer()
        //triggerCoroutineScopeError()
        //triggerViewAfterDestroy()
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
}