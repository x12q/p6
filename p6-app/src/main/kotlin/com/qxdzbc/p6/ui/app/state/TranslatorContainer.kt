package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.TranslatorMap
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.translator.TranslatorGetter


/**
 * A mutation layer over [TranslatorMap]
 */
interface TranslatorContainer : TranslatorGetter {
    /**
     * the returned translator is not attached to the app state. For testing only
     */
    fun getTranslatorOrCreate(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>
    /**
     * the returned translator is not attached to the app state. For testing only
     */
    fun getTranslatorOrCreate(wbWs: WbWs): P6Translator<ExUnit>
    fun getTranslatorOrCreate(wbWsSt: WbWsSt): P6Translator<ExUnit>
    fun getTranslatorOrCreate(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit>

    /**
     * Create a one-off translator that is not attached to the app state. For testing only
     */
    fun createOneOffTranslatorForTesting(wbKey: WorkbookKey, wsName: String):P6Translator<ExUnit>
    /**
     * Create a one-off translator that is not attached to the app state. For testing only
     */
    fun createOneOffTranslatorForTesting(wbWs: WbWs):P6Translator<ExUnit>
    /**
     * Create a one-off translator that is attached to the app state.
     */
    fun createOneOffTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>):P6Translator<ExUnit>

    fun addTranslator(key: WbWsSt, translator: P6Translator<ExUnit>)
    fun addTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>, translator: P6Translator<ExUnit>)

    fun removeTranslator(wbKey: WorkbookKey, wsName: String)
    fun removeTranslator(wbKey: WorkbookKey)
    fun removeTranslator(wbwsSt: WbWsSt)
    fun removeTranslator(wbWs: WbWs)
    fun removeTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>)
}
