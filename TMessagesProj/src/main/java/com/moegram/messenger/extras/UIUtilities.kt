package com.moegram.messenger.extras

import kotlinx.coroutines.*

import org.telegram.messenger.ApplicationLoader
import java.lang.Runnable

object UIUtilities {
    @JvmStatic
    fun runOnUIThread(runnable: Runnable) = ApplicationLoader.applicationHandler.post(runnable)

    fun runOnUIThread(runnable: () -> Unit) = ApplicationLoader.applicationHandler.post(runnable)

    @JvmStatic
    @JvmOverloads
    @DelicateCoroutinesApi
    fun runOnIoDispatcher(runnable: Runnable, delay: Long = 0) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delay)
            runnable.run()
        }
    }

    @DelicateCoroutinesApi
    fun runOnIoDispatcher(runnable: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            runnable()
        }
    }
}