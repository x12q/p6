package com.emeraldblast.p6.translator

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit

data class TranslatorMapImp(
    private val m: Map<Pair<WorkbookKey, String>, P6Translator<ExUnit>> = emptyMap()
) : TranslatorMap {
    override fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>? {
        return m[Pair(wbKey, wsName)]
    }

    override fun addTranslator(wbKey: WorkbookKey, wsName: String, translator: P6Translator<ExUnit>): TranslatorMap {
        return this.copy(m = m + (Pair(wbKey, wsName) to translator))
    }

    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorMap {
        return this.copy(m = m - Pair(wbKey, wsName))
    }

    override fun removeTranslator(wbKey: WorkbookKey): TranslatorMap {
        return this.copy(m=m.filterKeys { key->key.first != wbKey })
    }
}
