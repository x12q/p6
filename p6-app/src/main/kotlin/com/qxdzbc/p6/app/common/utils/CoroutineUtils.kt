package com.qxdzbc.p6.app.common.utils

import kotlinx.coroutines.*

object CoroutineUtils {
    private fun launch(coroutineScope:CoroutineScope,
                       dispatcher:CoroutineDispatcher,
                       f : suspend ()->Unit): Job {
        return coroutineScope.launch(dispatcher) { f() }
    }
    fun makeLaunchOnDispatchers(coroutineScope:CoroutineScope,
                                dispatcher:CoroutineDispatcher):(suspend ()->Unit) -> Job{
        val rt:(suspend ()->Unit) -> Job = {
            launch(coroutineScope,dispatcher,it)
        }
        return rt
    }
    fun makeLaunchOnIO(coroutineScope: CoroutineScope):(suspend ()->Unit)->Job{
        return makeLaunchOnDispatchers(coroutineScope,Dispatchers.IO)
    }

    /**
     * create a function that will lauch a coroutine job on the Main thread
     */
    fun makeLaunchOnMain(coroutineScope: CoroutineScope):(suspend ()->Unit)->Job{
        return makeLaunchOnDispatchers(coroutineScope,Dispatchers.Main)
    }

}
