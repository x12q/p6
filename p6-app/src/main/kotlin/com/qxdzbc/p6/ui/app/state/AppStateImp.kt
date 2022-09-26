package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.script.ScriptContainer
import com.qxdzbc.p6.app.document.script.ScriptContainerImp
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.app.oddity.ErrorContainerImp
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.False
import com.qxdzbc.p6.di.state.app_state.*
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.ActiveWindowPointerImp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.unwrapError
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import java.nio.file.Path
import javax.inject.Inject

data class AppStateImp @Inject constructor(
    @False
    override val codeEditorIsOpen: Boolean,
    @AppOddityContMs
    override val errorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
    @WindowActivePointerMs
    override val activeWindowPointerMs: Ms<ActiveWindowPointer> = ms(ActiveWindowPointerImp(null)),
    @AppScriptContMs
    val appScriptContainerMs: Ms<ScriptContainer> = ms(ScriptContainerImp()),
    @CentralScriptContMs
    override val centralScriptContainerMs: Ms<CentralScriptContainer>,
    @CodeEditorStateMs
    override val codeEditorStateMs: Ms<CodeEditorState>,
    val windowStateFactory: WindowStateFactory,
    private val wbStateFactory: WorkbookStateFactory,
    @SubAppStateContainerMs
    override val subAppStateContMs: Ms<SubAppStateContainer>,
    @DocumentContainerMs
    override val docContMs: Ms<DocumentContainer>,
    @TranslatorContainerMs
    override val translatorContMs: Ms<TranslatorContainer>,
    @CellEditorStateMs
    override val cellEditorStateMs: Ms<CellEditorState>,
) : AppState,AbsSubAppStateContainer() {

    override var docCont by docContMs
    override var subAppStateCont by subAppStateContMs
    override var translatorContainer: TranslatorContainer by translatorContMs
    override val wbContMs: Ms<WorkbookContainer>
        get() = docCont.wbContMs
    override var cellEditorState: CellEditorState by cellEditorStateMs
    override val windowStateMsList: List<Ms<WindowState>> get() = this.subAppStateContMs.value.windowStateMsList
    override val wbStateContMs: Ms<WorkbookStateContainer>
        get() = subAppStateContMs.value.wbStateContMs
    override var errorContainer: ErrorContainer by errorContainerMs
    override var wbStateCont: WorkbookStateContainer by wbStateContMs
    override var activeWindowPointer: ActiveWindowPointer by activeWindowPointerMs
    override val activeWindowStateMs: Ms<WindowState>?
        get() = activeWindowPointer.windowId?.let{this.getWindowStateMsById(it)}
    override val activeWindowState: WindowState?
        get() = activeWindowStateMs?.value
    override var wbCont: WorkbookContainer by wbContMs
    override var centralScriptContainer: CentralScriptContainer by centralScriptContainerMs
    override var codeEditorState: CodeEditorState by codeEditorStateMs

    override fun getWbWsSt(wbKey: WorkbookKey, wsName: String): WbWsSt? {
        return docCont.getWbWsSt(wbKey, wsName)
    }

    override fun getWbWsSt(wbWs: WbWs): WbWsSt? {
        return docCont.getWbWsSt(wbWs)
    }

    override fun getWbKeySt(wbKey: WorkbookKey): St<WorkbookKey>? {
        TODO("Not yet implemented")
    }

    override fun getWbKeyStRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>> {
        TODO("Not yet implemented")
    }

    override fun getWbKeyMs(wbKey: WorkbookKey): Ms<WorkbookKey>? {
        TODO("Not yet implemented")
    }

    override fun getWbKeyMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>> {
        TODO("Not yet implemented")
    }

    override fun getWsNameSt(wbKey: WorkbookKey, wsName: String): St<String>? {
        TODO("Not yet implemented")
    }

    override fun getWsNameSt(wbws: WbWs): St<String>? {
        TODO("Not yet implemented")
    }

    override fun getWsNameSt(wbKeySt: St<WorkbookKey>, wsName: String): St<String>? {
        TODO("Not yet implemented")
    }

    override fun getWsNameStRs(wbws: WbWs): Rse<St<String>> {
        TODO("Not yet implemented")
    }

    override fun getWsNameMs(wbKey: WorkbookKey, wsName: String): Ms<String>? {
        TODO("Not yet implemented")
    }

    override fun getWsNameMs(wbKeySt: St<WorkbookKey>, wsName: String): Ms<String>? {
        TODO("Not yet implemented")
    }

    override fun getWbRs(wbKey: WorkbookKey): Rs<Workbook, ErrorReport> {
        return docCont.getWbRs(wbKey)
    }

    override fun getWbRs(path: Path): Result<Workbook, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWbMsRs(wbKeySt: St<WorkbookKey>): Result<Ms<Workbook>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWbMsRs(wbKey: WorkbookKey): Result<Ms<Workbook>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWbMsRs(path: Path): Result<Ms<Workbook>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWb(wbKey: WorkbookKey): Workbook? {
        return docCont.getWb(wbKey)
    }

    override fun getWb(path: Path): Workbook? {
        TODO("Not yet implemented")
    }

    override fun getWbMs(wbKeySt: St<WorkbookKey>): Ms<Workbook>? {
        TODO("Not yet implemented")
    }

    override fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>? {
        TODO("Not yet implemented")
    }

    override fun getWbRs(wbKeySt: St<WorkbookKey>): Result<Workbook, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWsRs(wbKey: WorkbookKey, wsName: String): Rs<Worksheet, ErrorReport> {
        return docCont.getWsRs(wbKey, wsName)
    }

    override fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Worksheet, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWsRs(wbwsSt: WbWsSt): Rs<Worksheet, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWsRs(wbws: WbWs): Rs<Worksheet, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet? {
        return docCont.getWs(wbKey,wsName)
    }

    override fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet? {
        TODO("Not yet implemented")
    }

    override fun getWs(wbws: WbWs): Worksheet? {
        return docCont.getWs(wbws)
    }

    override fun getWs(wbwsSt: WbWsSt): Worksheet? {
        TODO("Not yet implemented")
    }

    override fun getWs(wsId: WorksheetIdWithIndexPrt): Worksheet? {
        TODO("Not yet implemented")
    }

    override fun getWsMsRs(wbKey: WorkbookKey, wsName: String): Rs<Ms<Worksheet>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Ms<Worksheet>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWsMsRs(wbwsSt: WbWsSt): Rs<Ms<Worksheet>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWsMsRs(wbws: WbWs): Rs<Ms<Worksheet>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>? {
        TODO("Not yet implemented")
    }

    override fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>? {
        TODO("Not yet implemented")
    }

    override fun getWsMs(wbws: WbWs): Ms<Worksheet>? {
        TODO("Not yet implemented")
    }

    override fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>? {
        TODO("Not yet implemented")
    }

    override fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport> {
        return docCont.getRangeRs(wbKey, wsName, rangeAddress)
    }

    override fun getRangeRs(rangeId: RangeId): Rs<Range, ErrorReport> {
        return docCont.getRangeRs(rangeId)
    }

    override fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range? {
        return docCont.getRange(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range? {
        return docCont.getLazyRange(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRangeRs(
        wbKey: WorkbookKey,
        wsName: String,
        rangeAddress: RangeAddress
    ): Rs<Range, ErrorReport> {
        return docCont.getLazyRangeRs(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRangeRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        rangeAddress: RangeAddress
    ): Rs<Range, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getCellRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Cell, ErrorReport> {
        return docCont.getCellRs(wbKey, wsName, cellAddress)
    }

    override fun getCellRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rs<Cell, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getCellRs(cellId: CellIdDM): Rs<Cell, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getCell(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell? {
        return docCont.getCell(wbKey, wsName, cellAddress)
    }

    override fun getCell(cellId: CellIdDM): Cell? {
        TODO("Not yet implemented")
    }

    override fun getCellMsRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Ms<Cell>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getCellMsRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rs<Ms<Cell>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getCellMsRs(cellId: CellIdDM): Rs<Ms<Cell>, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun getCellMs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Ms<Cell>? {
        TODO("Not yet implemented")
    }

    override fun getCellMs(cellId: CellIdDM): Ms<Cell>? {
        TODO("Not yet implemented")
    }

    override fun getWb(wbKeySt: St<WorkbookKey>): Workbook? {
        TODO("Not yet implemented")
    }

    override fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2> {
        return subAppStateCont.getStateByWorkbookKeyRs(workbookKey)
    }

    override fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>> {
        return subAppStateCont.getWbStateMsRs(wbKey)
    }

    override fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>> {
        return subAppStateCont.getWsStateMsRs(wbKey, wsName)
    }

    override fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, ErrorReport> {
        return subAppStateCont.getWindowStateMsByWbKeyRs(wbKey)
    }

    override fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, ErrorReport> {
        return subAppStateCont.getFocusStateMsByWbKeyRs(wbKey)
    }
    override var windowStateMap: Map<String, Ms<OuterWindowState>> by windowStateMapMs
    override val outerWindowStateMsList: List<Ms<OuterWindowState>>
        get() = this.subAppStateCont.outerWindowStateMsList
    override val windowStateMapMs: Ms<Map<String, Ms<OuterWindowState>>>
        get() = this.subAppStateContMs.value.windowStateMapMs

    override fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, ErrorReport> {
        return subAppStateCont.getWindowStateMsByIdRs(windowId)
    }

    override fun addOuterWindowState(windowState: Ms<OuterWindowState>): SubAppStateContainer {
        TODO("Not yet implemented")
    }

    override fun removeOuterWindowState(windowState: Ms<OuterWindowState>): SubAppStateContainer {
        TODO("Not yet implemented")
    }

    override fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>? {
        return subAppStateCont.getCursorStateMs(wbKey, wsName)
    }

    override fun openCodeEditor(): AppState {
        return this.copy(codeEditorIsOpen = true)
    }

    override fun closeCodeEditor(): AppState {
        return this.copy(codeEditorIsOpen = false)
    }

    override fun replaceWb(newWb: Workbook): AppState {
        docCont = docCont.replaceWb(newWb)
        return this
    }

    override fun addWbStateFor(wb: Workbook): AppState {
        subAppStateCont = subAppStateCont.addWbStateFor(wb)
        return this
    }

    override fun removeWindowState(windowState: Ms<WindowState>): AppState {
        subAppStateCont = subAppStateCont.removeWindowState(windowState)
        return this
    }

    override fun removeWindowState(windowId: String): AppState {
        subAppStateCont = subAppStateCont.removeWindowState(windowId)
        return this
    }

    override fun createNewWindowStateMs(): Pair<AppState, Ms<OuterWindowState>> {
        val p = subAppStateCont.createNewWindowStateMs()
        subAppStateCont = p.first
        return Pair(this, p.second)
    }

    override fun createNewWindowStateMs(windowId: String): Pair<AppState, Ms<OuterWindowState>> {
        val p = subAppStateCont.createNewWindowStateMs(windowId)
        subAppStateCont = p.first
        return Pair(this, p.second)
    }

    override fun getWbStateMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<WorkbookState>> {
        return this.subAppStateContMs.value.getWbStateMsRs(wbKeySt)
    }

    override fun addWindowState(windowState: Ms<WindowState>): AppState {
        subAppStateCont = subAppStateCont.addWindowState(windowState)
        return this
    }

    override fun queryStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult {
        val windowStateMsRs = this.getWindowStateMsByWbKeyRs(workbookKey)
        if (windowStateMsRs is Ok) {
            val oWindowstateMs = windowStateMsRs.value
            val workbookStateMsRs = wbStateCont.getWbStateMsRs(workbookKey)
            val windowstateMs = oWindowstateMs
            if (workbookStateMsRs is Ok) {

                return QueryByWorkbookKeyResult(
                    windowStateOrNull = windowstateMs,
                    workbookStateMsOrNull = workbookStateMsRs.value,
                    errorContainerMs = windowstateMs.value.errorContainerMs
                )
            } else {
                return QueryByWorkbookKeyResult(
                    _errorReport = workbookStateMsRs.unwrapError(),
                    errorContainerMs = windowstateMs.value.errorContainerMs
                )
            }
        } else {
            return QueryByWorkbookKeyResult(
                _errorReport = windowStateMsRs.unwrapError(),
                errorContainerMs = this.errorContainerMs
            )
        }
    }
}
