package com.emeraldblast.p6.di.rpc

import com.emeraldblast.p6.di.P6Singleton
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ReactiveRpcServerQualifier

