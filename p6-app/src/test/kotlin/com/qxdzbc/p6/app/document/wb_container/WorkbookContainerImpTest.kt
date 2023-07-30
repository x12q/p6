package com.qxdzbc.p6.app.document.wb_container

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.StateUtils.toMs
import test.TestSample
import kotlin.test.*

class WorkbookContainerImpTest{
    lateinit var cont: WorkbookContainerImp
    lateinit var wb1: Workbook
    lateinit var wb2: Workbook
    lateinit var wb3: Workbook
    lateinit var wbList:List<Workbook>

    @BeforeTest
    fun beforeEach() {
        val testSample = TestSample()
        wb1 = WorkbookImp(keyMs = WorkbookKey("wb1", null).toMs(), emptyList())
        wb2 = WorkbookImp(keyMs = WorkbookKey("wb2", null).toMs(), emptyList())
        wb3 = WorkbookImp(keyMs = WorkbookKey("wb3", null).toMs(), emptyList())
        val workbookList = listOf(wb1, wb2, wb3)
        wbList=workbookList
        val wbsContMs=testSample.sc.wbStateCont
        wbsContMs.apply{
            removeAll()
            createNewWbState(wb1)
            createNewWbState(wb2)
            createNewWbState(wb3)
        }

        cont = WorkbookContainerImp(testSample.sc.wbStateCont,testSample.comp.workbookStateFactory())
    }


    @Test
    fun getWorkbook() {
        assertEquals(wb1, cont.getWb(wb1.key))
        assertEquals(wb2, cont.getWb(wb2.key))
        assertEquals(wb3, cont.getWb(wb3.key))
    }

    @Test
    fun removeWorkbook() {
        cont.removeWb(wb1.key)
        assertNull(cont.getWb(wb1.key))
    }

    @Test
    fun getWorkbookList() {
        assertEquals(wbList, cont.allWbs)
    }
}
