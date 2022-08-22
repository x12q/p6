package com.qxdzbc.p6.ui.document.workbook.state

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.document.script.ScriptContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar.SheetTabBarState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState

/**
 * State of a workbook view
 */
interface WorkbookState {
    /**
     * the id in which a workbook belong to
     */
    val windowId:String?
    fun setWindowId(windowId:String?):WorkbookState
    val wsStateMap: Map<St<String>, MutableState<WorksheetState>>

    fun overWriteWb(newWb:Workbook):WorkbookState
    fun overWriteWbRs(newWb:Workbook): Rse<WorkbookState>

    val scriptContMs:Ms<ScriptContainer>
    var scriptCont:ScriptContainer

    fun refresh():WorkbookState

    /**
     * whether this workbook holds unsaved content or not
     */
    val needSave:Boolean
    fun setNeedSave(i:Boolean):WorkbookState

    /**
     * A stack of [Command], for undoing actions
     */
    val commandStackMs:Ms<CommandStack>
    var commandStack:CommandStack
    fun addCommand(command: Command):WorkbookState

    /**
     * The data obj of shown on the workbook view
     */
    val wbMs: Ms<Workbook>
    val wb: Workbook
    val wbKey:WorkbookKey
    /**
     * point this wb state to a new workbook by setting its workbook key and refresh this state and all child state to reflect this changes if necessary
     */
    fun setWorkbookKeyAndRefreshState(newWbKey: WorkbookKey): WorkbookState

    /**
     * refresh all child states so that the view state reflect the underlying data.
     */
    fun refreshWsState():WorkbookState

    /**
     * A list of all worksheet state
     */
    val worksheetStateListMs: List<Ms<WorksheetState>>
    val worksheetStateList: List<WorksheetState> get() = this.worksheetStateListMs.map { it.value }

    /**
     * state of sheet tab bar
     */
    val sheetTabBarState: SheetTabBarState

    /**
     * An obj indicate which worksheet is currently selected and shown on the screen.
     */
    val activeSheetPointerMs: Ms<ActiveWorksheetPointer>
    var activeSheetPointer: ActiveWorksheetPointer


    /**
     * state of the current active worksheet
     */
    val activeSheetStateMs: MutableState<WorksheetState>?
        get() = activeSheetPointer.wsName?.let {
            getWsStateMs(it)
        }
    val activeSheetState: WorksheetState? get() = activeSheetStateMs?.value

    /**
     * get worksheet state by sheet name
     */
    fun getWsState(sheetName: String): WorksheetState?
    fun getWsStateMs(sheetName: String): Ms<WorksheetState>?
    fun getWsStateMsRs(sheetName: String): Rse<Ms<WorksheetState>>

    fun getWsState(wsNameSt: St<String>): WorksheetState?
    fun getWsStateMs(wsNameSt: St<String>): Ms<WorksheetState>?
    fun getWsStateMsRs(wsNameSt: St<String>): Rse<Ms<WorksheetState>>

    /**
     * set active worksheet by name
     */
    fun setActiveSheet(sheetName: String): WorkbookState

    /**
     * set workbook key, effectively point this state to another workbook
     */
    fun setWorkbookKey(newWbKey: WorkbookKey): WorkbookState
    fun refreshWsPointer(): WorkbookState
}
