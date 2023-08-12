package com.qxdzbc.p6.ui.window.menu.action

import com.qxdzbc.p6.document_data_layer.wb_container.WorkbookContainer
import com.qxdzbc.p6.ui.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.composite_actions.window.WindowAction
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
    lateinit var windowState: WindowState
    lateinit var windowAction: WindowAction
    lateinit var ts:TestSample
    lateinit var wbCont:WorkbookContainer
    lateinit var wbStateCont:WorkbookStateContainer
    @BeforeTest
    fun b() {
        ts = TestSample()
        wbCont = ts.wbCont
        wbStateCont = ts.sc.wbStateCont
        windowState = ts.sampleWindowStateMs
        windowAction = mock<WindowAction>() {
            doNothing().whenever(it).saveActiveWorkbook(any(), any())
            doNothing().whenever(it).openSaveFileDialog(any())
        }
        action = FileMenuActionImp(
            windowAction = windowAction,
            stateCont = ts.sc,
            closeWbAct = ts.comp.closeWbAct()
        )
    }

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

        wbCont.replaceKey(TestSample.wbk1, validPathKey)
        wbStateCont.replaceKey(TestSample.wbk1, validPathKey)

        assertNotNull(windowState.activeWbState?.wbKey?.path)
        action.save(windowState.id)
        verify(windowAction, times(0)).openSaveFileDialog(windowState.id)
        verify(windowAction, times(1)).saveActiveWorkbook(any(), any())
    }
}
