package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.TranslatorMapImp
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.StateUtils.toMs
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

internal class TranslatorContainerImpTest {
    lateinit var tc: TranslatorContainer
    lateinit var ts: TestSample
    val t1 = mock<P6Translator<ExUnit>>()
    val t2 = mock<P6Translator<ExUnit>>()
    val t3 = mock<P6Translator<ExUnit>>()
    val t4 = mock<P6Translator<ExUnit>>()
    val wsn1Ms = TestSample.wsn1.toMs()
    val wsn2Ms = TestSample.wsn2.toMs()

    @BeforeTest
    fun b() {
        ts = TestSample()
        tc = TranslatorContainerImp(
            attachedTranslatorMapMs = TranslatorMapImp().toMs(),
            independentTranslatorMap = mutableMapOf(),
            translatorFactory = ts.comp.translatorFactory(),
            visitorFactory = ts.comp.visitorFactory2()
        )
    }

    @Test
    fun `getTranslator, addTranslator, remove translator`() {
        assertNull(tc.getTranslator(ts.wbKey1, ts.wsn1))
        assertNull(tc.getTranslator(ts.wbKey1Ms, wsn1Ms))

        tc.addTranslator(wbKeySt = ts.wbKey1Ms, wsNameSt = wsn1Ms, translator = t1)

        assertNotNull(tc.getTranslator(ts.wbKey1, ts.wsn1))
        assertNotNull(tc.getTranslator(ts.wbKey1Ms, wsn1Ms))
        assertNull(tc.getTranslator(ts.wbKey1Ms, ts.wsn1.toMs()))

        with(tc){
            addTranslator(wbKeySt = ts.wbKey1Ms, wsNameSt = wsn2Ms, translator = t2)
            addTranslator(ts.wbKey2Ms, wsn1Ms, t3)
            addTranslator(ts.wbKey2Ms, wsn2Ms, t4)
            addTranslator(ts.wbKey3Ms, wsn1Ms, t4)
            addTranslator(ts.wbKey3Ms, wsn2Ms, t2)
            addTranslator(WbWsSt(ts.wbKey4Ms, wsn1Ms),t2)
            addTranslator(WbWsSt(ts.wbKey4Ms, wsn2Ms),t2)

        }

        assertNotNull(tc.getTranslator(ts.wbKey1, wsn2Ms.value))
        tc.removeTranslator(ts.wbKey1, wsn2Ms.value)
        assertNull(tc.getTranslator(ts.wbKey1, wsn2Ms.value))

        assertNotNull(tc.getTranslator(ts.wbKey1Ms, wsn1Ms))
        tc.removeTranslator(ts.wbKey1Ms, wsn1Ms)
        assertNull(tc.getTranslator(ts.wbKey1Ms, wsn1Ms))

        assertNotNull(tc.getTranslator(ts.wbKey2Ms, wsn1Ms))
        assertNotNull(tc.getTranslator(ts.wbKey2Ms, wsn2Ms))
        tc.removeTranslator(ts.wbKey2)
        assertNull(tc.getTranslator(ts.wbKey2Ms, wsn1Ms))
        assertNull(tc.getTranslator(ts.wbKey2Ms, wsn2Ms))

        assertNotNull(tc.getTranslator(ts.wbKey3Ms, wsn1Ms))
        tc.removeTranslator(WbWsSt(ts.wbKey3Ms, wsn1Ms))
        assertNull(tc.getTranslator(ts.wbKey3Ms, wsn1Ms))

        assertNotNull(tc.getTranslator(ts.wbKey4Ms, wsn1Ms))
        tc.removeTranslator(WbWs(ts.wbKey4, wsn1Ms.value))
        assertNull(tc.getTranslator(ts.wbKey4Ms, wsn1Ms))
    }

    @Test
    fun getTranslatorOrCreate() {
        assertNull(tc.getTranslator(ts.wbKey1, ts.wsn1))
        val z = tc.getTranslatorOrCreate(ts.wbKey1, ts.wsn1)
        assertEquals(z, tc.getTranslator(ts.wbKey1, ts.wsn1))
        assertEquals(z, tc.getTranslator(ts.wbKey1Ms, wsn1Ms))
        assertEquals(z, tc.getTranslator(WbWsSt(ts.wbKey1Ms, wsn1Ms)))
        assertEquals(z, tc.getTranslator(WbWs(ts.wbKey1Ms.value, wsn1Ms.value)))

        assertNull(tc.getTranslator(ts.wbKey1, ts.wsn2))
        val q = tc.getTranslatorOrCreate(WbWsSt(ts.wbKey1Ms, wsn2Ms))
        assertEquals(q, tc.getTranslator(ts.wbKey1, ts.wsn2))
        assertEquals(q, tc.getTranslator(ts.wbKey1Ms, wsn2Ms))
        assertEquals(q, tc.getTranslator(WbWsSt(ts.wbKey1Ms, wsn2Ms)))
        assertEquals(q, tc.getTranslator(WbWs(ts.wbKey1Ms.value, wsn2Ms.value)))

        assertNull(tc.getTranslator(ts.wbKey2, ts.wsn1))
        val k = tc.getTranslatorOrCreate(ts.wbKey2Ms,wsn1Ms)
        assertEquals(k, tc.getTranslator(ts.wbKey2, ts.wsn1))
        assertEquals(k, tc.getTranslator(ts.wbKey2Ms, wsn1Ms))
        assertEquals(k, tc.getTranslator(WbWsSt(ts.wbKey2Ms, wsn1Ms)))
        assertEquals(k, tc.getTranslator(WbWs(ts.wbKey2Ms.value, wsn1Ms.value)))
    }
}
