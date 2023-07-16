package com.qxdzbc.p6.ui.window.action


class WindowActionImpTest {

//    lateinit var action: WindowActionImp
//    lateinit var windowRM: WindowRM
//    lateinit var appRM: AppRM
//    lateinit var windowState: Ms<WindowState>
//    val windowState: WindowState get() = windowState.value
//    lateinit var appStateMs: Ms<AppState>
//    lateinit var workbookEventApplier: WorkbookEventApplier
//    lateinit var windowHandlingLogic: WindowEventApplier
//    lateinit var appHandlingLogic: AppApplier
//    lateinit var testSample: TestSample
//
//    @BeforeTest
//    fun b() {
//        testSample = TestSample()
//        appStateMs = testSample.sampleAppStateMs()
//        action = aa()
//
//    }
//
//    fun aa(): WindowActionImp {
//        windowState = appStateMs.value.windowStateMsList.first()
//        windowRM = mock<WindowRM> {}
//        workbookEventApplier = mock()
//        windowHandlingLogic = mock()
//        appHandlingLogic = mock()
//        appRM = mock()
//        val action = WindowActionImp(
//            appStateMs = appStateMs,
//            appScope = mock(),
//            appApplier = appHandlingLogic,
//            appRM = appRM,
//            errorRouter = mock(),
//            kernelAction = mock(),
//            closeWbAction = mock()
//        )
//        return action
//    }
//

//
//    @Test
//    fun openLoadFileDialog() {
//        val a = aa()
//        assertFalse(windowState.value.loadDialogState.isOpen)
//        a.openLoadFileDialog()
//        assertTrue(windowState.value.loadDialogState.isOpen)
//    }
//
//    @Test
//    fun closeOpenFileDialog() {
//        val a = aa()
//        windowState.value.loadDialogState = windowState.value.loadDialogState.setOpen(true)
//        a.closeLoadFileDialog()
//        assertFalse { windowState.value.loadDialogState.isOpen }
//    }
//
//
//    @Test
//    fun openSaveFileDialog() {
//        val a = aa()
//        assertFalse(windowState.value.saveDialogState.isOpen)
//        a.openSaveFileDialog()
//        assertTrue(windowState.value.saveDialogState.isOpen)
//    }
//
//    @Test
//    fun closeSaveFileDialog() {
//        val a = aa()
//        windowState.value.saveDialogStateMs.value = windowState.value.saveDialogState.setOpen(true)
//        a.closeSaveFileDialog()
//        assertFalse { windowState.value.saveDialogState.isOpen }
//    }
//
//    @Test
//    fun saveWorkbook() {
//        val p = Path.of("folder/file23.txt")
//        val wbKey = WorkbookKey("file", Path.of("file.txt"))
//        val res = SaveWorkbookResponse(
//            isError = false,
//            errorReport = null, WorkbookKey(""), ""
//        )
//        val expectedRequest = SaveWorkbookRequest(wbKey, p.toAbsolutePath().toString())
//        // x: std case
//        whenever(appRM.saveWb(any())) doReturn res
//        action.saveWorkbook(wbKey, p)
//        verify(appRM, times(1)).saveWb(expectedRequest)
//        verify(appHandlingLogic, times(1)).applySaveWorkbook(res)
//    }
//
//    @Test
//    fun `saveActiveWorkbook null response`() {
//        val p = Path.of("folder/file23.txt")
//        val activeWbKey = WorkbookKey("file", Path.of("file.txt"))
//        val expectedRequest = SaveWorkbookRequest(activeWbKey, p.toAbsolutePath().toString())
//        // x: std case
//        whenever(appRM.saveWb(any())) doReturn null
//        action.saveWorkbook(activeWbKey, p)
//        verify(appRM, times(1)).saveWb(expectedRequest)
//        verify(appHandlingLogic, times(0)).applySaveWorkbook(any())
//    }
//
//    @Test
//    fun saveActiveWorkbook() {
//        val activeWbKey = TestSample.sampleWbKey1
//        val p = Path.of("file.txt")
//        val spyAction = spy(action)
//        spyAction.saveActiveWorkbook(p)
//        verify(spyAction, times(1)).saveWorkbook(activeWbKey, p)
//    }
//
//    @Test
//    fun `saveActiveWorkbook null workbook key`() {
//        val p = Path.of("folder/file23.txt")
//        windowState.value = windowState.setWorkbookList(emptyList())
//        val spyAction = spy(action) {}
//        spyAction.saveActiveWorkbook(p)
//        verify(spyAction, times(0)).saveWorkbook(any(), any())
//    }
}
