package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.common.utils.CapHashMap
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.di.state.app_state.InitSingleTranslatorMap
import com.emeraldblast.p6.di.state.app_state.TranslatorMapMs
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.TranslatorMap
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaTranslatorFactory
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaVisitorFactory
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.common.compose.StateUtils.toSt
import javax.inject.Inject

/**
 * a mutable layer
 */
class TranslatorContainerImp @Inject constructor(
    @TranslatorMapMs
    val translatorMapMs: Ms<TranslatorMap>,
    @InitSingleTranslatorMap
    private val singleTranslatorMap: MutableMap<Pair<WorkbookKey,String>,P6Translator<ExUnit>>,
    private val translatorFactory: JvmFormulaTranslatorFactory,
    private val visitorFactory: JvmFormulaVisitorFactory,
) : TranslatorContainer {

    var tm: TranslatorMap by translatorMapMs

    override fun getTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
        return this.getTranslator(WbWsSt(wbKeySt, wsNameSt))
    }

    override fun createOneOffTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
        val oneOffTranslator = translatorFactory.create(
            visitor = visitorFactory.create(wbKey.toSt(),wsName.toSt())
        )
        return oneOffTranslator
    }

    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorContainer {
        translatorMapMs.value = translatorMapMs.value.removeTranslator(wbKey, wsName)
        return this
    }

    override fun removeTranslator(wbKey: WorkbookKey): TranslatorContainer {
        translatorMapMs.value = translatorMapMs.value.removeTranslator(wbKey)
        return this
    }

    override fun addTranslator(key: WbWsSt, translator: P6Translator<ExUnit>): TranslatorContainer {
        tm = tm.addTranslator(key, translator)
        return this
    }

    override fun addTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        translator: P6Translator<ExUnit>
    ): TranslatorContainer {
        tm = tm.addTranslator(wbKeySt, wsNameSt, translator)
        return this
    }

    override fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
        val t1 = tm.getTranslator(wbKey, wsName)
        if(t1!=null){
            return t1
        }else{
            val newTrans = createOneOffTranslator(wbKey, wsName)
            singleTranslatorMap.put((wbKey to wsName),newTrans)
            return newTrans
        }
    }

    override fun getTranslator(wbWsSt: WbWsSt): P6Translator<ExUnit> {
        val t= tm.getTranslator(wbWsSt)
        if(t!=null){
            return t
        }else{
            val newTrans = translatorFactory.create(
                visitor = visitorFactory.create(
                    wbKeySt = wbWsSt.wbKeySt, wsNameSt = wbWsSt.wsNameSt
                )
            )
            tm = tm.addTranslator(wbWsSt,newTrans)
            return newTrans
        }
    }

    override fun removeTranslator(key: WbWsSt): TranslatorContainer {
        tm = tm.removeTranslator(key)
        return this
    }

    override fun removeTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        translator: P6Translator<ExUnit>
    ): TranslatorContainer {
        tm = tm.removeTranslator(wbKeySt, wsNameSt, translator)
        return this
    }
}
