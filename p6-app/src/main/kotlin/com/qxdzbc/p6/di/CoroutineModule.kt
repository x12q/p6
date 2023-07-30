package com.qxdzbc.p6.di

import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

import dagger.Module
@Module
@OptIn(ExperimentalCoroutinesApi::class)
interface CoroutineModule {
    companion object{
        @Provides
        @ActionDispatcherMain
        fun actionDispatcherMain(): CoroutineDispatcher {
            return Dispatchers.Main.limitedParallelism(1)
        }
        @Provides
        @ActionDispatcherDefault
        fun actionDispatcherDefault(): CoroutineDispatcher {
            return Dispatchers.Default.limitedParallelism(1)
        }
    }
}
