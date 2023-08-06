package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.qualifiers.InitSingleTranslatorMap
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.TranslatorMap
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.jvm_translator.ExUnitFormulaTranslatorFactory
import com.qxdzbc.p6.translator.jvm_translator.ExUnitFormulaVisitorFactory
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding

import javax.inject.Inject
import javax.inject.Singleton

/**
 * a mutable layer
 */
@Singleton
@ContributesBinding(P6AnvilScope::class)
class TranslatorContainerImp @Inject constructor(
    val attachedTranslatorMapMs: Ms<TranslatorMap>,
    @InitSingleTranslatorMap
    private val independentTranslatorMap: MutableMap<Pair<WorkbookKey,String>,P6Translator<ExUnit>>,
    private val translatorFactory: ExUnitFormulaTranslatorFactory,
    private val visitorFactory: ExUnitFormulaVisitorFactory,
) : TranslatorContainer {

    private var tm: TranslatorMap by attachedTranslatorMapMs

    override fun getTranslatorOrCreate(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
        return this.getTranslatorOrCreate(WbWsSt(wbKeySt, wsNameSt))
    }

    override fun getTranslatorOrCreate(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
        val t1 = tm.getTranslator(wbKey, wsName)
        if(t1!=null){
            return t1
        }else{
            val newTrans = createOneOffTranslatorForTesting(wbKey, wsName)
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


    override fun createOneOffTranslatorForTesting(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
        val oneOffTranslator = translatorFactory.create(
            visitor = visitorFactory.create(wbKey.toSt(),wsName.toSt())
        )
        return oneOffTranslator
    }

    override fun createOneOffTranslatorForTesting(wbWs: WbWs): P6Translator<ExUnit> {
        return this.createOneOffTranslatorForTesting(wbWs.wbKey,wbWs.wsName)
    }

    override fun createOneOffTranslator(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
        return this.createOneOffTranslatorForTesting(wbKeySt.value,wsNameSt.value)
    }

    override fun removeTranslator(wbKey: WorkbookKey, wsName: String) {
        attachedTranslatorMapMs.value = attachedTranslatorMapMs.value.removeTranslator(wbKey, wsName)
    }

    override fun removeTranslator(wbKey: WorkbookKey) {
        attachedTranslatorMapMs.value = attachedTranslatorMapMs.value.removeTranslator(wbKey)
    }

    override fun addTranslator(key: WbWsSt, translator: P6Translator<ExUnit>) {
        tm = tm.addTranslator(key, translator)
    }

    override fun addTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        translator: P6Translator<ExUnit>
    ) {
        tm = tm.addTranslator(wbKeySt, wsNameSt, translator)
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

    override fun removeTranslator(wbwsSt: WbWsSt) {
        tm = tm.removeTranslator(wbwsSt)
    }

    override fun removeTranslator(wbWs: WbWs) {
        tm = tm.removeTranslator(wbWs)
    }

    override fun removeTranslator(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
    ) {
        tm = tm.removeTranslator(wbKeySt, wsNameSt)
    }
}
