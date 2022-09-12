package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.common.path.PPath
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.ui.file.P6FileLoaderErrors
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.TestSample
import java.nio.file.Path
import kotlin.io.path.toPath
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LoadWorkbookActionImpTest {
    lateinit var ts: TestSample
    lateinit var action: LoadWorkbookAction

    @BeforeTest
    fun b() {
        ts = TestSample()
        action = ts.p6Comp.loadWorkbookAction()
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
