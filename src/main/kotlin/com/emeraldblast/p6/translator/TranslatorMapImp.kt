package com.emeraldblast.p6.translator

import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.St

data class TranslatorMapImp(
    private val m: Map<WbWsSt, P6Translator<ExUnit>> = emptyMap(),
) : TranslatorMap, Map<WbWsSt, P6Translator<ExUnit>> by m {
    override fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>? {
        val k = m.keys
            .firstOrNull { it.wbKey == wbKey && it.wsName == wsName }
        val rt = k?.let { m[k] }
        return rt
    }

    override fun getTranslator(wbWsSt: WbWsSt): P6Translator<ExUnit>? {
        return m[wbWsSt]
    }

    override fun addTranslator(wbKey: WorkbookKey, wsName: String, translator: P6Translator<ExUnit>): TranslatorMap {
        throw UnsupportedOperationException()
    }

    override fun addTranslator(key: WbWsSt, translator: P6Translator<ExUnit>): TranslatorMap {
        val nm = m + (key to translator)
        return this.copy(m = nm)
    }

    override fun addTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        translator: P6Translator<ExUnit>
    ): TranslatorMap {
        return this.addTranslator(WbWsSt(wbKeySt, wsNameSt), translator)
    }

    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorMap {
        val newM = m.filterKeys { it.wbKey != wbKey && it.wsName != wsName }
        return this.copy(m = newM)
    }

    override fun removeTranslator(wbKey: WorkbookKey): TranslatorMap {
        val newM = m.filterKeys { it.wbKey != wbKey }
        return this.copy(m = newM)
    }
}
