package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.di.state.app_state.TranslatorMapMs
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.TranslatorMap
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaTranslatorFactory
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaVisitorFactory
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import javax.inject.Inject

class TranslatorContainerImp @Inject constructor(
    @TranslatorMapMs
    val translatorMapMs: Ms<TranslatorMap>,
    private val translatorFactory: JvmFormulaTranslatorFactory,
    private val visitorFactory: JvmFormulaVisitorFactory,
) : TranslatorContainer,TranslatorMap by translatorMapMs.value {

    var translatorMap: TranslatorMap by translatorMapMs

    override fun getTranslatorOrCreate(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
        return this.getTranslatorOrCreate(WbWsSt(wbKeySt, wsNameSt))
    }

    override fun getTranslatorOrCreate(wbWsSt: WbWsSt): P6Translator<ExUnit> {
        val trans = this.translatorMap.getTranslator(wbWsSt)
        if (trans != null) {
            return trans
        } else {
            val newTranslator = translatorFactory.create(
                visitor = visitorFactory.create(wbWsSt.wbKeySt,wbWsSt.wsNameSt)
            )
            this.translatorMap = this.translatorMap.addTranslator(wbWsSt, newTranslator)
            return newTranslator
        }
    }

    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorContainer {
        translatorMapMs.value = translatorMapMs.value.removeTranslator(wbKey, wsName)
        return this
    }

    override fun removeTranslator(wbKey: WorkbookKey): TranslatorContainer {
        translatorMapMs.value = translatorMapMs.value.removeTranslator(wbKey)
        return this
    }
}
