package com.emeraldblast.p6.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NullInt

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class False

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FalseMs

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class True()

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TrueMs

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Fake()

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Username()

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppZContext

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationCoroutineScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EventServerSocket

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EventServerPort

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Null

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultTextFieldValue

