package com.emeraldblast.p6.ui.app.state

import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.TranslatorMap
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St


/**
 * A mutation layer over [TranslatorMap]
 */
interface TranslatorContainer : TranslatorMap {

    override fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>

    override fun getTranslator(wbWsSt: WbWsSt): P6Translator<ExUnit>

    /**
     * Create a one-off translator that is used once then discarded.
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

    override fun removeTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        translator: P6Translator<ExUnit>
    ): TranslatorContainer

    override fun getTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit>
}
