package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import test.BaseAppStateTest
import test.TestSample
import kotlin.test.*

class CloseWorkbookActionImpTest : BaseAppStateTest() {

    lateinit var closeWbAct: CloseWorkbookActionImp
    lateinit var res: CloseWorkbookResponse

    @BeforeTest
    fun b() {
        closeWbAct = ts.comp.closeWbAct()
        val windowId = sc.windowStateMsList[0].id
        res = CloseWorkbookResponse(
            wbKey = TestSample.wbk1,
            windowId = windowId,
            errorReport = null
        )
    }

    @org.junit.Test
    fun internalApply_1() {
        assertNotNull(sc.getWbState(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))
        closeWbAct.internalApply(res.wbKey, res.windowId)
        assertNull(sc.getWbState(TestSample.wbk1))
        assertNull(sc.wbCont.getWb(TestSample.wbk1))
    }

    @org.junit.Test
    fun `apply null window id`() {
        val res = this.res.copy(windowId = null)
        assertNotNull(sc.getWbState(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))

        closeWbAct.internalApply(res.wbKey, null)

        assertNull(sc.getWbState(TestSample.wbk1))
        assertNull(sc.wbCont.getWb(TestSample.wbk1))
        assertNull(sc.wbStateCont.getWbState(TestSample.wbk1))
    }

    @org.junit.Test
    fun `apply null window id invalid workbook key`() {
        val res = this.res.copy(windowId = null, wbKey = WorkbookKey("invalid"))

        assertNotNull(sc.getWbState(TestSample.wbk1))
        assertNotNull(sc.getWbState(TestSample.wbk2))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk2))

        closeWbAct.internalApply(res.wbKey, null)

        assertNotNull(sc.getWbState(TestSample.wbk1))
        assertNotNull(sc.getWbState(TestSample.wbk2))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk2))
    }


    @Test
    fun `close 1 wb`() {
        val wbk = ts.wbKey1
        val windowState = sc.getWindowStateMsById(ts.window1Id)

        assertNotNull(windowState)
        assertTrue(wbk in windowState.wbKeySet)
        assertNotNull(sc.getWb(wbk))

        closeWbAct.closeWb(
            CloseWorkbookRequest(
                wbKey = wbk,
                windowId = ts.window1Id
            )
        )

        assertTrue(wbk !in windowState.wbKeySet)
        assertNull(sc.getWb(wbk))
    }

    @Test
    fun `test state obj after closeWb`() {
        val wbk = ts.wbKey1
        test("state obj after removing wb") {
            val input = CloseWbState(
                wbCont = ts.sc.wbCont,
                respectiveWindowState = ts.sc.getWindowStateByWbKey(wbk)
            )
            preCondition {
                input.wbCont.containWb(wbk) shouldBe true
                input.wbCont.getWb(wbk) shouldNotBe null
                wbk shouldBeIn input.respectiveWindowState?.wbKeySet!!
            }
            val out = closeWbAct.closeWb(ts.wbKey1Ms, input)
            postCondition {
                out.wbCont.containWb(wbk) shouldBe false
                out.wbCont.getWb(wbk) shouldBe null
                wbk shouldNotBeIn out.respectiveWindowState?.wbKeySet!!
            }
        }
    }

    @Test
    fun `test state assignment after closeWb`() {
        val wbk = ts.wbKey1
        val windowState = ts.sc.getWindowStateMsByWbKey(wbk)
        test("state obj after removing wb") {
            preCondition {
                ts.sc.wbCont.containWb(wbk) shouldBe true
                ts.sc.wbCont.getWb(wbk) shouldNotBe null
                wbk shouldBeIn windowState?.wbKeySet!!
            }
            closeWbAct.closeWb(ts.wbKey1Ms)
            postCondition {
                ts.sc.wbCont.containWb(wbk) shouldBe false
                ts.sc.wbCont.getWb(wbk) shouldBe null
                wbk shouldNotBeIn windowState?.wbKeySet!!
            }
        }
    }
}

