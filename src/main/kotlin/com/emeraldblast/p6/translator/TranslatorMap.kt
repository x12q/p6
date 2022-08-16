package com.emeraldblast.p6.translator

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit

interface TranslatorMap {
    fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>?
    fun addTranslator(wbKey: WorkbookKey, wsName: String, translator: P6Translator<ExUnit>): TranslatorMap
    fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorMap
    fun removeTranslator(wbKey: WorkbookKey): TranslatorMap
}
