package com.qxdzbc.p6.di.rpc

import com.qxdzbc.p6.di.P6Singleton
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ReactiveRpcServerQualifier

