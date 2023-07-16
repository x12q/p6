package com.qxdzbc.p6.app.action.workbook.remove_all_ws

import com.github.michaelbull.result.Ok
import test.TestSample
import kotlin.test.*

internal class RemoveAllWorksheetActionImpTest {
    lateinit var ts: TestSample
    lateinit var act: RemoveAllWorksheetAction

    @BeforeTest
    fun b() {
        ts = TestSample()
        act = ts.comp.removeAllWorksheetAction()
    }

    @Test
    fun removeAllWs() {
        val wbk = ts.wbKey1
        val wbStateMs = ts.sc.getWbState(wbk)
        assertNotNull(wbStateMs)

        assertTrue(wbStateMs.wb.isNotEmpty())
        assertTrue(wbStateMs.worksheetStateList.isNotEmpty())
        assertTrue(wbStateMs.activeSheetPointer.isValid())

        val rs = act.removeAllWsRs(wbk)

        assertTrue(rs is Ok)
        assertTrue(wbStateMs.wb.isEmpty())
        assertTrue(wbStateMs.worksheetStateList.isEmpty())
        assertFalse(wbStateMs.activeSheetPointer.isValid())
    }
}
