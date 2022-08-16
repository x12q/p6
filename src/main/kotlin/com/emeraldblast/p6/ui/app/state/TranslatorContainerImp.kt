package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.di.state.app_state.TranslatorMapMs
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.TranslatorMap
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaTranslatorFactory
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaVisitorFactory
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class TranslatorContainerImp @Inject constructor(
    @TranslatorMapMs
    val translatorMapMs: Ms<TranslatorMap>,
    val translatorFactory: JvmFormulaTranslatorFactory,
    val visitorFactory: JvmFormulaVisitorFactory,
) : TranslatorContainer,TranslatorMap by translatorMapMs.value {
    var translatorMap: TranslatorMap by translatorMapMs

    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorContainer {
        translatorMapMs.value = translatorMapMs.value.removeTranslator(wbKey, wsName)
        return this
    }

    override fun removeTranslator(wbKey: WorkbookKey): TranslatorContainer {
        translatorMapMs.value = translatorMapMs.value.removeTranslator(wbKey)
        return this
    }

    override fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
        val trans = this.translatorMap.getTranslator(wbKey, wsName)
        if (trans != null) {
            return trans
        } else {
            val newTranslator = translatorFactory.create(
                visitor = visitorFactory.create(wbKey, wsName)
            )
            this.translatorMap = this.translatorMap.addTranslator(wbKey, wsName, newTranslator)
            return newTranslator
        }
    }
}
