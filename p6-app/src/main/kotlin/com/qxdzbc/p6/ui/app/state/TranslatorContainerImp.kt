package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.InitSingleTranslatorMap
import com.qxdzbc.p6.di.state.app_state.TranslatorMapMs
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.TranslatorMap
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaTranslatorFactory
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaVisitorFactory
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import javax.inject.Inject

/**
 * a mutable layer
 */
class TranslatorContainerImp @Inject constructor(
    @TranslatorMapMs
    val attachedTranslatorMapMs: Ms<TranslatorMap>,
    @InitSingleTranslatorMap
    private val independentTranslatorMap: MutableMap<Pair<WorkbookKey,String>,P6Translator<ExUnit>>,
    private val translatorFactory: JvmFormulaTranslatorFactory,
    private val visitorFactory: JvmFormulaVisitorFactory,
//    @StateContainerSt
//    val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : TranslatorContainer {

//    private val sc by stateContSt

    private var tm: TranslatorMap by attachedTranslatorMapMs

    override fun getTranslatorOrCreate(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
        return this.getTranslatorOrCreate(WbWsSt(wbKeySt, wsNameSt))
    }

    override fun getTranslatorOrCreate(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
        val t1 = tm.getTranslator(wbKey, wsName)
        if(t1!=null){
            return t1
        }else{
            val newTrans = createOneOffTranslator(wbKey, wsName)
            independentTranslatorMap.put((wbKey to wsName),newTrans)
            return newTrans
        }
    }

    override fun getTranslatorOrCreate(wbWs: WbWs): P6Translator<ExUnit> {
        return this.getTranslatorOrCreate(wbWs.wbKey,wbWs.wsName)
    }

    override fun getTranslatorOrCreate(wbWsSt: WbWsSt): P6Translator<ExUnit> {
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


    override fun createOneOffTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
        val oneOffTranslator = translatorFactory.create(
            visitor = visitorFactory.create(wbKey.toSt(),wsName.toSt())
        )
        return oneOffTranslator
    }

    override fun createOneOffTranslator(wbWs: WbWs): P6Translator<ExUnit> {
        return this.createOneOffTranslator(wbWs.wbKey,wbWs.wsName)
    }

    override fun createOneOffTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
        return this.createOneOffTranslator(wbKeySt.value,wsNameSt.value)
    }

    override fun removeTranslator(wbKey: WorkbookKey, wsName: String): TranslatorContainer {
        attachedTranslatorMapMs.value = attachedTranslatorMapMs.value.removeTranslator(wbKey, wsName)
        return this
    }

    override fun removeTranslator(wbKey: WorkbookKey): TranslatorContainer {
        attachedTranslatorMapMs.value = attachedTranslatorMapMs.value.removeTranslator(wbKey)
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

    override fun getTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit>? {
        val t1 = this.tm.getTranslator(wbKeySt, wsNameSt)
        if(t1!=null){
            return t1
        }
        val t2=this.independentTranslatorMap.get(wbKeySt.value to wsNameSt.value)
        return t2
    }

    override fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>? {
        val t1 = this.tm.getTranslator(wbKey, wsName)
        if(t1!=null){
            return t1
        }
        val t2=this.independentTranslatorMap.get(wbKey to wsName)
        return t2
    }

    override fun getTranslator(wbWsSt: WbWsSt): P6Translator<ExUnit>? {
        val t1 = this.tm.getTranslator(wbWsSt)
        if(t1!=null){
            return t1
        }
        val t2=this.independentTranslatorMap.get(wbWsSt.wbKey to wbWsSt.wsName)
        return t2
    }

    override fun getTranslator(wbWs: WbWs): P6Translator<ExUnit>? {
        return this.getTranslator(wbWs.wbKey,wbWs.wsName)
    }

    override fun removeTranslator(key: WbWsSt): TranslatorContainer {
        tm = tm.removeTranslator(key)
        return this
    }

    override fun removeTranslator(wbWs: WbWs): TranslatorContainer {
        tm = tm.removeTranslator(wbWs)
        return this
    }

    override fun removeTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
    ): TranslatorContainer {
        tm = tm.removeTranslator(wbKeySt, wsNameSt)
        return this
    }
}
