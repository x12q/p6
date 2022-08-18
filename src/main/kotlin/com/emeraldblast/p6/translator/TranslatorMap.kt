package com.emeraldblast.p6.translator

import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.St

interface TranslatorMap {
    fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>?
    fun getTranslator(wbWsSt: WbWsSt): P6Translator<ExUnit>?

    fun addTranslator(wbKey: WorkbookKey, wsName: String, translator: P6Translator<ExUnit>): TranslatorMap
    fun addTranslator(key: WbWsSt, translator: P6Translator<ExUnit>): TranslatorMap
    fun addTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>, translator: P6Translator<ExUnit>): TranslatorMap

    fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorMap
    fun removeTranslator(wbKey: WorkbookKey): TranslatorMap
}
