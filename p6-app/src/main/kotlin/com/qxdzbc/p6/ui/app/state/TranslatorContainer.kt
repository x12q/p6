package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.TranslatorMap
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St


/**
 * A mutation layer over [TranslatorMap]
 */
interface TranslatorContainer : TranslatorMap {
    fun getTranslatorOrCreate(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>
    fun getTranslatorOrCreate(wbWsSt: WbWsSt): P6Translator<ExUnit>
    fun getTranslatorOrCreate(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit>

    /**
     * Create a one-off translator that is used once and then must be discarded.
     */
    fun createOneOffTranslator(wbKey: WorkbookKey,wsName: String):P6Translator<ExUnit>
    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorContainer
    override fun removeTranslator(wbKey: WorkbookKey): TranslatorContainer


    override fun addTranslator(key: WbWsSt, translator: P6Translator<ExUnit>): TranslatorContainer

    override fun addTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        translator: P6Translator<ExUnit>
    ): TranslatorContainer


    override fun removeTranslator(key: WbWsSt): TranslatorContainer
    override fun removeTranslator(wbWs: WbWs): TranslatorContainer
    override fun removeTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): TranslatorContainer
}
