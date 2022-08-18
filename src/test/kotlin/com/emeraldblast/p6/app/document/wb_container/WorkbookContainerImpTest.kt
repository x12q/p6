package com.emeraldblast.p6.app.document.wb_container

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import test.TestSample
import kotlin.test.*

class WorkbookContainerImpTest{
    lateinit var cont: WorkbookContainerImp2
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
        val wbsContMs=testSample.appState.globalWbStateContMs
        wbsContMs.value =wbsContMs.value.removeAll().createNewWbState(wb1).createNewWbState(wb2).createNewWbState(wb3)

        cont = WorkbookContainerImp2(testSample.appState.globalWbStateContMs,testSample.p6Comp.workbookStateFactory())
    }


    @Test
    fun getWorkbook() {
        assertEquals(wb1, cont.getWb(wb1.key))
        assertEquals(wb2, cont.getWb(wb2.key))
        assertEquals(wb3, cont.getWb(wb3.key))
    }

    @Test
    fun addOrOverWriteWorkbook() {
        val wb4 = WorkbookImp(WorkbookKey("wb4",null).toMs(), listOf())
        val c2 = cont.addOrOverWriteWb(wb4)
        assertNotNull(c2.getWb(wb4.key))
    }

    @Test
    fun removeWorkbook() {
        val c2 = cont.removeWb(wb1.key)
        assertNull(c2.getWb(wb1.key))
    }

    @Test
    fun getWorkbookList() {
        assertEquals(wbList, cont.wbList)
    }
}
