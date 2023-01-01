package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.path.PPath
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.file.P6FileLoaderErrors
import com.qxdzbc.p6.ui.format2.CellFormatTable
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.BaseAppStateTest
import test.TestSample
import java.nio.file.Path
import kotlin.io.path.toPath
import kotlin.test.*

class LoadWorkbookActionImpTest : BaseAppStateTest(){
    lateinit var action: LoadWorkbookActionImp
    lateinit var scMs: Ms<StateContainer>
    lateinit var errorRouter: ErrorRouter

    @BeforeTest
    fun b() {
        action = ts.comp.loadWorkbookActionImp()
        scMs = ts.scMs
        errorRouter = ErrorRouterImp(scMs,ts.appState.errorContainerMs)
    }

    @Test
    fun `applyLoadWorkbook std case`() {
        val windowId = scMs.value.windowStateMsList[0].value.id
//        val wb = WorkbookImp(WorkbookKey("Book33").toMs())
        val wb = Workbook.random()
        ts.stateContMs().value.wbCont = ts.stateContMs().value.wbCont.addWb(wb)
        val cellFormatTableMap = wb.worksheets.associate{
            it.name to CellFormatTable.random()
        }



        action.apply(windowId, wb, cellFormatTableMap,null,null)
        postCondition {
            val wbState=ts.sc.getWbState(wb.key)
            wbState.shouldNotBeNull()
            wbState.wb shouldBe wb
            wb.worksheets.forEach { ws->
                val tb = ts.sc.getCellFormatTable(WbWs(wb.key,ws.name))
                tb.shouldNotBeNull()
                tb shouldBe cellFormatTableMap[ws.name]
            }
        }
    }

    @Test
    fun `apply Load Workbook into invalid window`() {
        val windowId = "invalid wd id"
        val wb = WorkbookImp(WorkbookKey("Book33").toMs())
        ts.stateContMs().value.wbCont = ts.stateContMs().value.wbCont.addWb(wb)
        action.apply(windowId, wb,null,null,null)
        val wds = ts.sc.getWindowStateMsById(windowId)
        assertNotNull(wds)
        assertEquals(listOf(wb), wds.value.wbList)
        assertEquals(listOf(wb.key), wds.value.wbStateList.map { it.wbKey })
    }

    @Test
    fun load() {
        val path = this::class.java.getResource("/sampleWb/w1.txt")?.toURI()?.toPath()
        assertNotNull(path)
        val pp = PPaths.get(path)
        val o = action.loadWorkbook(
            LoadWorkbookRequest(
                path = pp,
                windowId = ts.window1Id
            )
        )
        assertTrue(o.isOk)
    }

    @Test
    fun `load error path`() {
        val p = Path.of("file.qwe")

        val absPath = mock<PPath> { z ->
            whenever(z.toString()).thenReturn(p.toString())
        }

        val ppMap = mapOf(
            // x: path is not a file
            mock<PPath> {
                whenever(it.isRegularFile()) doReturn false
                whenever(it.exists()) doReturn true
                whenever(it.isReadable()) doReturn true
                whenever(it.toAbsolutePath()) doReturn absPath
                whenever(it.toString()) doReturn "p1"
            }.let { it to P6FileLoaderErrors.notAFile(it) },
            // x: path is not readable
            mock<PPath> {
                whenever(it.isRegularFile()) doReturn true
                whenever(it.exists()) doReturn true
                whenever(it.isReadable()) doReturn false
                whenever(it.toAbsolutePath()) doReturn absPath
                whenever(it.toString()) doReturn "p2"
            }.let { it to P6FileLoaderErrors.notReadableFile(it) },
            // x: path does not exist
            mock<PPath> {
                whenever(it.isRegularFile()) doReturn true
                whenever(it.exists()) doReturn false
                whenever(it.isReadable()) doReturn true
                whenever(it.toAbsolutePath()) doReturn absPath
                whenever(it.toString()) doReturn "p3"
            }.let { it to P6FileLoaderErrors.notAFile(it) }
        )
        for ((ppath, err) in ppMap) {
            val req = LoadWorkbookRequest(
                path = ppath,
                windowId = ts.window1Id
            )
            val o = action.loadWorkbook(req)
            assertTrue(o.isError)
            assertTrue(o.errorReport?.isType(err) ?: false, ppath.toString())
        }
    }
}
