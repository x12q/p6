package com.qxdzbc.p6.di

import javax.inject.Qualifier

/**
 * To annotate "DoNothing" version of action objects
 */
@Qualifier
annotation class DoNothing

@Qualifier
annotation class PartialTreeExtractor

@Qualifier
annotation class PartialCellRangeExtractorQ

@Qualifier
annotation class TextElementVisitorQ

@Qualifier
annotation class NullInt

@Qualifier
annotation class False

@Qualifier
annotation class FalseMs

@Qualifier
annotation class True

@Qualifier
annotation class TrueMs

@Qualifier
annotation class Fake

@Qualifier
annotation class Username

@Qualifier
annotation class AppCoroutineScope

@Qualifier
annotation class EventServerPort

@Qualifier
annotation class Null

@Qualifier
annotation class DefaultTextFieldValue

@Qualifier
annotation class ActionDispatcherMain

@Qualifier
annotation class ActionDispatcherDefault
