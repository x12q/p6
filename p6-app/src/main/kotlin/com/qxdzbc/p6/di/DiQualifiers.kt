package com.qxdzbc.p6.di

import javax.inject.Qualifier

/**
 * To annotate that certain action should be injected with a "DoNothing" version
 */
@Qualifier
annotation class Dont

@Qualifier
@Retention
annotation class PartialTreeExtractor

@Qualifier
@Retention
annotation class PartialCellRangeExtractorQ

@Qualifier
@Retention
annotation class TextElementVisitorQ

@Qualifier
@Retention
annotation class NullInt

@Qualifier
@Retention
annotation class False

@Qualifier
@Retention
annotation class FalseMs

@Qualifier
@Retention
annotation class True

@Qualifier
@Retention
annotation class TrueMs

@Qualifier
@Retention
annotation class Fake

@Qualifier
@Retention
annotation class Username

@Qualifier
@Retention
annotation class AppCoroutineScope

@Qualifier
@Retention
annotation class EventServerSocket

@Qualifier
@Retention
annotation class EventServerPort

@Qualifier
@Retention
annotation class Null

@Qualifier
@Retention
annotation class DefaultTextFieldValue

@Qualifier
@Retention
annotation class ActionDispatcherMain

@Qualifier
@Retention
annotation class ActionDispatcherDefault
