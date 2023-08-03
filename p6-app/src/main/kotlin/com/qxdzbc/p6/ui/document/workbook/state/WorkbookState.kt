package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.common.P6ExperimentalApi
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.di.comp.WbComponent
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar.SheetTabBarState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState

/**
 * State of a workbook
 */
interface WorkbookState : CanConvertToWorkbookProto{
    /**
     * the window id in which a workbook belong to. This shows which window this workbook state belong to.
     * This way, I can pass a [WorkbookState] around, and still know which window it belong to. Without this, in order to find out which window this belongs to, I would need to iterate through the list of window, and find the window containing the key of this [WorkbookState].
     */
    var windowId:String?

    val wsStateMap: Map<St<String>, WorksheetState>

    @P6ExperimentalApi("Use this function with care.The action of forced overwriting a workbook is very destructive. Must be considered careful before use")
    fun overWriteWb(newWb:Workbook)

    @P6ExperimentalApi("Use this function with care.The action of forced overwriting a workbook is very destructive. Must be considered careful before use")
    fun overWriteWbRs(newWb:Workbook): Rse<Unit>

    /**
     * Some child state objects (Ws pointer and Ws state) contain a pseudo state variable to force refreshing.
     * This function invokes all the refresh functions of such objects.
     */
    fun refresh()

    /**
     * whether this workbook holds unsaved content or not
     */
    var needSave:Boolean

    /**
     * The data obj shown on the workbook view
     */
    val wbMs: Ms<Workbook>
    val wb: Workbook

    // need to expose the MS, because State container relies on this
    val wbKey:WorkbookKey
    val wbKeyMs:Ms<WorkbookKey>

    /**
     * Find the workbook having key == [newWbKey], and tied this state to that [Workbook]
     */
    fun setWorkbookKeyAndRefreshState(newWbKey: WorkbookKey)

    /**
     * refresh all child states so that the view state reflect the underlying data.
     */
    fun refreshWsState()

    /**
     * A list of all worksheet state
     */
    val worksheetStateListMs: List<WorksheetState>
    val worksheetStateList: List<WorksheetState> get() = this.worksheetStateListMs

    /**
     * produce a derived state for sheet tab bar
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
    val activeSheetStateMs: WorksheetState?
        get() = activeSheetPointer.wsName?.let {
            getWsStateMs(it)
        }
    val activeSheetState: WorksheetState? get() = activeSheetStateMs

    /**
     * get worksheet state by sheet name
     */
    fun getWsState(sheetName: String): WorksheetState?
    fun getWsStateMs(sheetName: String): WorksheetState?
    fun getWsStateMsRs(sheetName: String): Rse<WorksheetState>

    fun getWsState(wsNameSt: St<String>): WorksheetState?
    fun getWsStateMs(wsNameSt: St<String>): WorksheetState?
    fun getWsStateMsRs(wsNameSt: St<String>): Rse<WorksheetState>

    /**
     * set active worksheet by name
     */
    fun setActiveSheet(sheetName: String)

    /**
     * set workbook key, effectively point this state to another workbook
     */
    fun setWbKey(newWbKey: WorkbookKey)
    fun refreshWsPointer()
}
