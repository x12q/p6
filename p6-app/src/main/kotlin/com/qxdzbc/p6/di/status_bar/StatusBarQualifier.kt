package com.qxdzbc.p6.di.status_bar

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class KernelStatusItemStateQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RPCStatusItemStateQualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class StatusBarStateQualifier
