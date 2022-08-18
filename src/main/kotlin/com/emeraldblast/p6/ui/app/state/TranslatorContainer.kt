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
    /**
     * Attempt to get a translator, if such translator does not exist, create a new one, append the new one to the current container and return it.
     */
    fun getTranslatorOrCreate(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit>

    /**
     * Attempt to get a translator, if such translator does not exist, create a new one, append the new one to the current container and return it.
     */
    fun getTranslatorOrCreate(wbWsSt:WbWsSt): P6Translator<ExUnit>
    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorContainer
    override fun removeTranslator(wbKey: WorkbookKey): TranslatorContainer
}
