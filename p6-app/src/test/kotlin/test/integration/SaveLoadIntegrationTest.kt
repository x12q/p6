package test.integration

import androidx.compose.runtime.getValue
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import test.TestSample
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.toPath
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SaveLoadIntegrationTest {
    lateinit var ts: TestSample
    lateinit var saveAct: SaveWorkbookAction
    lateinit var loadAct: LoadWorkbookAction

    @BeforeTest
    fun b() {
        ts = TestSample()
        saveAct = ts.p6Comp.saveWbAction()
        loadAct = ts.p6Comp.loadWbAction()
    }

    @Test
    fun `load then save to a different file`() {
        // x: precondition
        val sct by ts.stateContMs()
        val originalPath = this.javaClass.getResource("/sampleWb/w1.txt")?.toURI()?.toPath()
        assertNotNull(originalPath)
        val wbk = WorkbookKey("w1.txt", originalPath)
        loadAct.loadWorkbook(LoadWorkbookRequest(PPaths.get(originalPath), null))
        val wbkSt = sct.getWbKeySt(wbk)
        assertNotNull(wbkSt)
        assertNotNull(sct.getWb(wbk))
        // x: save the wb
        val savePath = Paths.get("twb.txt")
        saveAct.saveWorkbook(wbkSt.value, savePath)
        // x: post condition
        assertEquals(savePath.fileName.toString(), wbkSt.value.name)
        assertEquals(savePath.toAbsolutePath(), wbkSt.value.path?.toAbsolutePath())
        val wb = ts.stateContMs().value.getWb(wbkSt.value)
        assertNotNull(wb)
        if (savePath.exists()) {
            Files.delete(savePath)
        }
    }
}
