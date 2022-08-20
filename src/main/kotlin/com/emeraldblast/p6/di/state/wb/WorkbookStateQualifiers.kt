package com.emeraldblast.p6.di.state.wb

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultCommandStack

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultWsStateMap

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultScriptContMs
