package com.qxdzbc.p6.app.action.workbook.remove_all_ws

import androidx.compose.runtime.getValue
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
        val wbStateMs = ts.sc.getWbStateMs(wbk)
        assertNotNull(wbStateMs)
        val wbState by wbStateMs

        assertTrue(wbState.wb.isNotEmpty())
        assertTrue(wbState.worksheetStateList.isNotEmpty())
        assertTrue(wbState.activeSheetPointer.isValid())

        val rs = act.removeAllWsRs(wbk)

        assertTrue(rs is Ok)
        assertTrue(wbState.wb.isEmpty())
        assertTrue(wbState.worksheetStateList.isEmpty())
        assertFalse(wbState.activeSheetPointer.isValid())
    }
}
