package com.qxdzbc.p6.ui.document.workbook.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.FalseMs
import com.qxdzbc.p6.di.state.wb.DefaultWsStateMap
import com.qxdzbc.p6.di.state.ws.DefaultActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar.SheetTabBarState
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar.SheetTabBarStateImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateFactory
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetIdImp
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory.Companion.createThenRefresh
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class WorkbookStateImp @AssistedInject constructor(
    @Assisted("1") override val wbMs: Ms<Workbook>,
    @Assisted("2") val windowIdMs: Ms<String?>,
    // ======================================= //
    @DefaultWsStateMap
    override val wsStateMap: Map<St<@JvmSuppressWildcards String>, @JvmSuppressWildcards MutableState<WorksheetState>>,
    @DefaultActiveWorksheetPointer
    override val activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
    @FalseMs private val needSaveMs: Ms<Boolean>,
    private val wsStateFactory: WorksheetStateFactory,
    private val gridSliderFactory: LimitedGridSliderFactory,
    private val cursorStateFactory: CursorStateFactory,
    private val thumbStateFactory: ThumbStateFactory,
) : BaseWorkbookState() {

    override val windowId: String? by windowIdMs

    override val needSave: Boolean by needSaveMs

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
        val rt = wsStateMap.values.firstOrNull { it.value.wsName == sheetName }
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
        val w = getWsStateMs(wsNameSt)
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

    override fun refresh(): WorkbookState {
        return this.refreshWsPointer().refreshWsState()
    }

    override fun setNeedSave(i: Boolean): WorkbookState {
        needSaveMs.value = i
        return this
    }

    override var wb: Workbook by wbMs

    override val wbKey: WorkbookKey
        get() = wb.key
    override val wbKeyMs: Ms<WorkbookKey>
        get() = wb.keyMs

    /**
     * point the workbook inside this state to a new workbook key, then refresh the state.
     */
    override fun setWorkbookKeyAndRefreshState(newWbKey: WorkbookKey): WorkbookState {
        val newWb = wb.setKey(newWbKey)
        this.wb = newWb
        // Assign new active sheet name if need
        val newActiveSheetName = this.pickActiveSheet(newWb)
        activeSheetPointer = activeSheetPointer.pointTo(newActiveSheetName)
        val rt = this.refreshWsState()
        return rt
    }

    override fun refreshWsState(): WorkbookState {
        var newStateMap: Map<St<String>, MutableState<WorksheetState>> = mutableMapOf()
        val sheetList: List<Ms<Worksheet>> = this.wb.worksheetMsList
        for (wsMs: Ms<Worksheet> in sheetList) {
            val ws: Worksheet = wsMs.value
            val wsStateMs: Ms<WorksheetState>? = this.getWsStateMs(ws.name)
            if (wsStateMs != null) {
                // x: keep the existing state
                wsStateMs.value = wsStateMs.value.refreshCellState()
                newStateMap = newStateMap + (wsStateMs.value.wsNameSt to wsStateMs)
            } else {
                // x: create new state for new sheet
                val newState = this.createDefaultWsState(wsMs)
                newStateMap = newStateMap + (newState.value.wsNameSt to newState)
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
                return this
            }
        } else {
            val activeSheetName = wb.getWs(0)?.nameMs
            this.activeSheetPointer = this.activeSheetPointer.pointTo(activeSheetName)
            return this
        }
    }

    override fun setWbKey(newWbKey: WorkbookKey): WorkbookState {
        this.wb = this.wb.setKey(newWbKey)
        return this
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
        val wsState = wsStateFactory.createThenRefresh(
            wsMs = wsMs,
            gridSliderFactory = this.gridSliderFactory,
            cursorStateFactory = this.cursorStateFactory,
            thumbStateFactory = this.thumbStateFactory

        )
        return ms(wsState.refreshCellState())
    }

    override fun setWindowId(i: String?): WorkbookState {
        windowIdMs.value = i
        return this
    }

    /**
     * TODO reconsider throwing exception here, it will crash the app?
     */
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

    companion object {
        fun default(
            wbMs: Ms<Workbook>,
            wsStateFactory: WorksheetStateFactory,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
            thumbStateFactory: ThumbStateFactory,
        ): WorkbookStateImp {
            val activeSheetName = wbMs.value.getWs(0)?.nameMs
            return default(
                wbMs = wbMs,
                activeSheetPointerMs = ms(ActiveWorksheetPointerImp(activeSheetName)),
                wsStateFactory = wsStateFactory,
                gridSliderFactory = gridSliderFactory,
                cursorStateFactory = cursorStateFactory,
                thumbStateFactory = thumbStateFactory
            )
        }

        fun default(
            wbMs: Ms<Workbook>,
            activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
            wsStateFactory: WorksheetStateFactory,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
            thumbStateFactory: ThumbStateFactory,
        ): WorkbookStateImp {
            val wsStateMap: Map<St<String>, Ms<WorksheetState>> = wbMs.value.worksheetMsList
                .map { wsMs ->
                    val wsIdMs: Ms<WorksheetId> = ms(
                        WorksheetIdImp(
                            wsNameMs = wsMs.value.nameMs,
                            wbKeySt = wbMs.value.keyMs,
                        )
                    )
                    val cursorIdMs: Ms<CursorId> = ms(CursorIdImp(wsIdMs))
                    val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>> = ms(emptyMap())
                    val mainCellMs = ms(CellAddresses.A1)
                    ms(
                        wsStateFactory.create(
                            wsMs = wsMs,
                            sliderMs = gridSliderFactory.create().toMs(),
                            cursorStateMs = cursorStateFactory.create(
                                idMs = cursorIdMs,
                                cellLayoutCoorsMapSt = cellLayoutCoorMapMs,
                                thumbStateMs = ms(
                                    thumbStateFactory.create(
                                        cursorIdSt = cursorIdMs,
                                        mainCellSt = mainCellMs,
                                        cellLayoutCoorMapSt = cellLayoutCoorMapMs
                                    )
                                ),
                                mainCellMs = mainCellMs
                            ).toMs(),
                            cellLayoutCoorMapMs = cellLayoutCoorMapMs
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
                thumbStateFactory = thumbStateFactory,
                windowIdMs = ms(null),
                needSaveMs = ms(false),
            )
        }
    }

}
