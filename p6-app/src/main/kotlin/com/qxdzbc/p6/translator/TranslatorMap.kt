package com.qxdzbc.p6.translator

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.St

interface TranslatorMap {
    fun getTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit>?
    fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>?
    fun getTranslator(wbWsSt: WbWsSt): P6Translator<ExUnit>?
    fun getTranslator(wbWs: WbWs): P6Translator<ExUnit>?

    fun addTranslator(key: WbWsSt, translator: P6Translator<ExUnit>): TranslatorMap
    fun addTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>, translator: P6Translator<ExUnit>): TranslatorMap

    fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorMap
    fun removeTranslator(wbKey: WorkbookKey): TranslatorMap
    fun removeTranslator(key: WbWsSt): TranslatorMap
    fun removeTranslator(wbWs: WbWs): TranslatorMap
    fun removeTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>, ): TranslatorMap
}
