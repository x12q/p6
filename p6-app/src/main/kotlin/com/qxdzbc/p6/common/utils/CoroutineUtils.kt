package com.qxdzbc.p6.common.utils

import kotlinx.coroutines.*
import java.util.concurrent.Executors

object CoroutineUtils {
    private fun launch(coroutineScope:CoroutineScope,
                       dispatcher:CoroutineDispatcher,
                       f : suspend ()->Unit): Job {
        return coroutineScope.launch(dispatcher) { f() }
    }

    private fun <T> asyncD(coroutineScope:CoroutineScope,
                       dispatcher:CoroutineDispatcher,
                       f : suspend ()->T): Deferred<T> {
        return coroutineScope.async(dispatcher) { f() }
    }
    fun makeLaunchOnDispatchers(coroutineScope:CoroutineScope,
                                dispatcher:CoroutineDispatcher):(suspend ()->Unit) -> Job{
        val rt:(suspend ()->Unit) -> Job = {
            launch(coroutineScope,dispatcher,it)
        }
        return rt
    }
    fun <T> makeAsyncOnDispatchers(coroutineScope:CoroutineScope,
                                dispatcher:CoroutineDispatcher):(suspend ()->T) -> Deferred<T>{
        val rt:(suspend ()->T) -> Deferred<T> = {
            asyncD(coroutineScope,dispatcher,it)
        }
        return rt
    }

    fun makeLaunchOnIO(coroutineScope: CoroutineScope):(suspend ()->Unit)->Job{
        return makeLaunchOnDispatchers(coroutineScope,Dispatchers.IO)
    }
    fun makeLaunchOnDefault(coroutineScope: CoroutineScope):(suspend ()->Unit)->Job{
        return makeLaunchOnDispatchers(coroutineScope,Dispatchers.Default)
    }

    fun <T> makeAsyncOnDefault(coroutineScope: CoroutineScope):(suspend ()->T)->Deferred<T>{
        return makeAsyncOnDispatchers(coroutineScope,Dispatchers.Default)
    }

    fun <T> makeAsyncOnIO(coroutineScope: CoroutineScope):(suspend ()->T)->Deferred<T>{
        return makeAsyncOnDispatchers(coroutineScope,Dispatchers.IO)
    }

    fun <T> makeAsyncOnMain(coroutineScope: CoroutineScope):(suspend ()->T)->Deferred<T>{
        return makeAsyncOnDispatchers(coroutineScope,Dispatchers.Main)
    }

    /**
     * create a function that will lauch a coroutine job on the Main thread
     */
    fun makeLaunchOnMain(coroutineScope: CoroutineScope):(suspend ()->Unit)->Job{
        return makeLaunchOnDispatchers(coroutineScope,Dispatchers.Main)
    }

}
