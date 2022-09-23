package com.qxdzbc.p6.ui.document.workbook.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.command.CommandStacks
import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.document.script.ScriptContainer
import com.qxdzbc.p6.app.document.script.ScriptContainerImp
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.True
import com.qxdzbc.p6.di.state.wb.DefaultCommandStack
import com.qxdzbc.p6.di.state.wb.DefaultScriptContMs
import com.qxdzbc.p6.di.state.wb.DefaultWsStateMap
import com.qxdzbc.p6.di.state.ws.DefaultActiveWorksheetPointer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar.SheetTabBarState
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar.SheetTabBarStateImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.qxdzbc.p6.ui.document.worksheet.state.*
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory.Companion.createRefresh
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class WorkbookStateImp @AssistedInject constructor(
    // NOTE: workbook state id is stored in a Ms because it is shared by multiple child states
    @Assisted("1") override val wbMs: Ms<Workbook>,
    @Assisted("2") override val windowId: String?,
    // ======================================= //
    @DefaultWsStateMap
    override val wsStateMap: Map< St<@JvmSuppressWildcards String>,@JvmSuppressWildcards MutableState<WorksheetState>>,
    @DefaultActiveWorksheetPointer
    override val activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
    @True private val refreshVar: Boolean = true,
    @True override val needSave: Boolean = true,
    @DefaultCommandStack
    override val commandStackMs: Ms<CommandStack> = ms(CommandStacks.stdCommandStack()),
    private val wsStateFactory: WorksheetStateFactory,
    private val gridSliderFactory: LimitedGridSliderFactory,
    private val cursorStateFactory: CursorStateFactory,
    @DefaultScriptContMs
    override val scriptContMs: Ms<ScriptContainer>,
) : WorkbookState {
    companion object {
        fun default(
            wbMs: Ms<Workbook>,
            wsStateFactory: WorksheetStateFactory,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
        ): WorkbookStateImp {
            val activeSheetName = wbMs.value.getWs(0)?.nameMs
            return default(
                wbMs,
                ms(ActiveWorksheetPointerImp(activeSheetName)),
                wsStateFactory = wsStateFactory,
                gridSliderFactory = gridSliderFactory,
                cursorStateFactory = cursorStateFactory,
            )
        }

        fun default(
            wbMs: Ms<Workbook>,
            activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
            wsStateFactory: WorksheetStateFactory,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
            scriptContMs: Ms<ScriptContainer> = ms(ScriptContainerImp())
        ): WorkbookStateImp {
            val wsStateMap: Map<St<String>, MutableState<WorksheetState>> = wbMs.value.worksheetMsList.map { wsMs ->
                val ws by wsMs
                val wsIdMs: Ms<WorksheetId> = ms(
                    WorksheetIdImp(
                        wsNameMs = ws.nameMs,
                        wbKeySt = wbMs.value.keyMs,
                    )
                )
                val cursorIdMs: Ms<CursorStateId> = ms(CursorIdImp(wsIdMs))
                ms(
                    wsStateFactory.create(
                        wsMs = wsMs,
                        sliderMs = gridSliderFactory.create().toMs(),
                        cursorStateMs = cursorStateFactory.create(
                            idMs = cursorIdMs,
                        ).toMs()
                    ) as WorksheetState
                )
            }.associateBy { it.value.wsNameSt }

            return WorkbookStateImp(
                wbMs = wbMs,
                activeSheetPointerMs = activeSheetPointerMs,
                wsStateMap = wsStateMap,
                wsStateFactory = wsStateFactory,
                gridSliderFactory = gridSliderFactory,
                cursorStateFactory = cursorStateFactory,
                scriptContMs = scriptContMs,
                windowId = null
            )

        }
    }

    override val worksheetStateListMs: List<Ms<WorksheetState>> get() = wsStateMap.values.toList()

    override val sheetTabBarState: SheetTabBarState
        get() = SheetTabBarStateImp(
            activeSheetPointerMs = activeSheetPointerMs,
            wbMs = wbMs
        )


    override var activeSheetPointer: ActiveWorksheetPointer by activeSheetPointerMs

    override fun getWsState(sheetName: String): WorksheetState? {
        return this.getWsStateMs(sheetName)?.value
    }

    override fun getWsState(wsNameSt: St<String>): WorksheetState? {
        return this.getWsStateMs(wsNameSt)?.value
    }

    override fun getWsStateMs(sheetName: String): MutableState<WorksheetState>? {
        val rt = wsStateMap.values.firstOrNull { it.value.wsName== sheetName }
        return rt
    }

    override fun getWsStateMs(wsNameSt: St<String>): Ms<WorksheetState>? {
        return wsStateMap[wsNameSt]
    }

    override fun getWsStateMsRs(sheetName: String): Rse<Ms<WorksheetState>> {
        val w = getWsStateMs(sheetName)
        return w?.let {
            Ok(it)
        }
            ?: Err(WorkbookStateErrors.WorksheetStateNotExist.report("Worksheet state for \"${sheetName}\" does not exist"))
    }

    override fun getWsStateMsRs(wsNameSt: St<String>): Rse<Ms<WorksheetState>> {
        val w=getWsStateMs(wsNameSt)
        return w?.let {
            Ok(it)
        }
            ?: Err(WorkbookStateErrors.WorksheetStateNotExist.report("Worksheet state for \"${wsNameSt.value}\" does not exist"))
    }

    override fun setActiveSheet(sheetName: String): WorkbookState {
        val ws = this.wb.getWs(sheetName)
        if (ws != null) {
            this.activeSheetPointer = this.activeSheetPointer.pointTo(ws.nameMs)
        }
        return this
    }

    override var scriptCont: ScriptContainer by scriptContMs


    override fun refresh(): WorkbookState {
        return this.refreshWsPointer().refreshWsState()
    }

    override fun setNeedSave(i: Boolean): WorkbookState {
        if (needSave != i) {
            return this.copy(needSave = i)
        } else {
            return this
        }
    }

    override var commandStack: CommandStack by commandStackMs
    override fun addCommand(command: Command): WorkbookState {
        commandStack = commandStack.add(command)
        return this
    }

    override var wb: Workbook by wbMs

    override val wbKey: WorkbookKey
        get() = wb.key
    override val wbKeyMs: Ms<WorkbookKey>
        get() = wb.keyMs

    /**
     * point this workbook state to a new workbook, then refresh the state.
     */
    override fun setWorkbookKeyAndRefreshState(newWbKey: WorkbookKey): WorkbookState {
        val newWb = wb.setKey(newWbKey)
        this.wb = newWb
        // x: assign new active sheet name if need
        val newActiveSheetName = this.pickActiveSheet(newWb)
        activeSheetPointer = activeSheetPointer.pointTo(newActiveSheetName)
        val rt = this.refreshWsState()
        return rt
    }

    override fun refreshWsState(): WorkbookState {
        var newStateMap: Map<St<String>, MutableState<WorksheetState>> = mutableMapOf()
        val sheetList: List<Ms<Worksheet>> = this.wb.worksheetMsList
        for (wsMs:Ms<Worksheet> in sheetList) {
            val ws: Worksheet = wsMs.value
            val wsStateMs: Ms<WorksheetState>? = this.getWsStateMs(ws.name)
            if (wsStateMs != null) {
                // x: keep the existing state
                wsStateMs.value = wsStateMs.value.refreshCellState()
                newStateMap= newStateMap + (wsStateMs.value.wsNameSt to wsStateMs)
            } else {
                // x: create new state for new sheet
                val newState = this.createDefaultWsState(wsMs)
                newStateMap= newStateMap+ (newState.value.wsNameSt to newState)
            }
        }
        return this.copy(wsStateMap = newStateMap)
    }

    override fun refreshWsPointer(): WorkbookState {
        val wb = this.wb
        val currentActiveSheetName = this.activeSheetPointer.wsName
        if (currentActiveSheetName != null) {
            if (wb.containSheet(currentActiveSheetName)) {
                return this
            } else {
                val activeSheetName = wb.getWs(0)?.nameMs
                this.activeSheetPointer = this.activeSheetPointer.pointTo(activeSheetName)
                return this.forceRefresh()
            }
        } else {
            val activeSheetName = wb.getWs(0)?.nameMs
            this.activeSheetPointer = this.activeSheetPointer.pointTo(activeSheetName)
            return this.forceRefresh()
        }
    }

    override fun setWbKey(newWbKey: WorkbookKey): WorkbookState {
        this.wb = this.wb.setKey(newWbKey)
        return this.forceRefresh()
    }

    private fun forceRefresh(): WorkbookState {
        return this.copy(refreshVar = !refreshVar)
    }

    /**
     * If the new book does not contain the old active sheet
     * -> set the 1st sheet in the new workbook as the new active sheet
     */
    private fun pickActiveSheet(newWorkbook: Workbook): St<String>? {
        val newWorkbook_Contain_ActiveSheet =
            this.activeSheetPointer.wsName?.let { newWorkbook.containSheet(it) } == true
        if (newWorkbook_Contain_ActiveSheet) {
            return this.activeSheetPointer.wsNameSt
        } else {
            if (newWorkbook.isNotEmpty()) {
                return newWorkbook.worksheets.first().nameMs
            } else {
                return null
            }
        }
    }

    private fun createDefaultWsState(worksheet: Ms<Worksheet>): Ms<WorksheetState> {
        val wsMs = worksheet
        val wsState = wsStateFactory.createRefresh(
            worksheetMs = wsMs,
            gridSliderFactory = this.gridSliderFactory,
            cursorStateFactory = this.cursorStateFactory,
        )
        return ms(wsState.refreshCellState())
    }

    override fun setWindowId(windowId: String?): WorkbookState {
        return this.copy(windowId = windowId)
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun overWriteWb(newWb: Workbook): WorkbookState {
        return this.overWriteWbRs(newWb).getOrThrow()
    }

    override fun overWriteWbRs(newWb: Workbook): Rse<WorkbookState> {
        if (newWb.key == this.wb.key) {
            this.wb = newWb
            return this.refresh().toOk()
        } else {
            return WorkbookStateErrors.CantOverWriteWorkbook.report("Can't overwrite workbook of this state (${this.wb.key}) because the workbook keys do not match")
                .toErr()
        }
    }
}
