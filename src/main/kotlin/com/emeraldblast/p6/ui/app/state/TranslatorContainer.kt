package com.emeraldblast.p6.ui.app.state

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.TranslatorMap
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.Ms

/**
 * store formula translator. Each translator is mapped to a pair of workbook key and worksheet name
 */
interface TranslatorContainer : TranslatorMap {
    override fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>
    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorContainer
    override fun removeTranslator(wbKey: WorkbookKey): TranslatorContainer
}
