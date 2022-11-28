package com.qxdzbc.p6.ui.window.menu.action

import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.ui.window.state.WindowState
import org.mockito.kotlin.*
import test.TestSample
import java.nio.file.Path
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class FileMenuActionImpTest {
    lateinit var action: FileMenuActionImp
    lateinit var windowStateMs: Ms<WindowState>
    lateinit var windowAction: WindowAction
    lateinit var ts:TestSample
    lateinit var wbContMs:Ms<WorkbookContainer>
    lateinit var wbStateContMs:Ms<WorkbookStateContainer>
    @BeforeTest
    fun b() {
        ts = TestSample()
        wbContMs = ts.wbContMs
        wbStateContMs = ts.sc.wbStateContMs
        windowStateMs = ts.sampleWindowStateMs
        windowAction = mock<WindowAction>() {
            doNothing().whenever(it).saveActiveWorkbook(any(), any())
            doNothing().whenever(it).openSaveFileDialog(any())
        }
        action = FileMenuActionImp(
            windowAction = windowAction,
            stateContMs = ts.scMs,
            closeWbAct = ts.comp.closeWbAct()
        )
    }

    val windowState get() = windowStateMs.value

    @Test
    fun `saveWorkbook when path is null`() {
        assertNotNull( windowState.activeWbState)
        assertNull(windowState.activeWbState?.wbKey?.path)
        action.save(windowState.id)
        verify(windowAction, times(1)).openSaveFileDialog(windowState.id)
        verify(windowAction, times(0)).saveActiveWorkbook(any(), any())
    }

    @Test
    fun `saveWorkbook when path is valid`() {
        val validPathKey = TestSample.wbk1.setPath(Path.of("sample/path"))
        assertNotNull(windowState.activeWbStateMs)

        wbContMs.value = wbContMs.value.replaceKey(TestSample.wbk1, validPathKey)
        wbStateContMs.value = wbStateContMs.value.replaceKey(TestSample.wbk1, validPathKey)

        assertNotNull(windowState.activeWbState?.wbKey?.path)
        action.save(windowState.id)
        verify(windowAction, times(0)).openSaveFileDialog(windowState.id)
        verify(windowAction, times(1)).saveActiveWorkbook(any(), any())
    }
}
