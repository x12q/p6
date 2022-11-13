package com.qxdzbc.p6.app.action.app.save_wb

import test.TestSample
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.test.*

class SaveWorkbookActionImpTest {
    lateinit var ts:TestSample
    lateinit var act:SaveWorkbookAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act = ts.comp.saveWbAction()
    }

    @Test
    fun saveWorkbook() {
        val path = Paths.get("twb.txt")
        act.saveWorkbook(ts.wbKey1,path)
        assertEquals(path.fileName.toString(),ts.wbKey1.name)
        assertEquals(path.toAbsolutePath(),ts.wbKey1.path?.toAbsolutePath())
        val wb = ts.stateContMs().value.getWb(ts.wbKey1)
        assertNotNull(wb)
        if(path.exists()){
            Files.delete(path)
        }
    }
}
