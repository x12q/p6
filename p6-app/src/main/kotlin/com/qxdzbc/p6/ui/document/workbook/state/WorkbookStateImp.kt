package com.qxdzbc.p6.ui.document.workbook.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.P6ExperimentalApi
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
import com.qxdzbc.p6.ui.document.worksheet.di.DefaultActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.document.workbook.di.comp.NeedSave
import com.qxdzbc.p6.ui.document.workbook.di.comp.WindowIdMsInWbState
import com.qxdzbc.p6.ui.document.workbook.di.comp.WsStateMapMs
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
import javax.inject.Inject

/**
 *
 */
data class WorkbookStateImp @Inject constructor(
    override val wbMs: Ms<Workbook>,
    @WindowIdMsInWbState
    private val windowIdMs: Ms<String?>,
    @WsStateMapMs
    private val wsStateMapMs: Ms<Map<St<String>, WorksheetState>>,
    @DefaultActiveWorksheetPointer
    override val activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
    @NeedSave
    private val needSaveMs: Ms<Boolean>,
    private val wsStateFactory: WorksheetStateFactory,
    private val gridSliderFactory: LimitedGridSliderFactory,
    private val cursorStateFactory: CursorStateFactory,
    private val thumbStateFactory: ThumbStateFactory,
) : BaseWorkbookState() {

    override val wsStateMap: Map<St<String>, WorksheetState> by wsStateMapMs

    override var windowId: String? by windowIdMs

    override var needSave: Boolean by needSaveMs

    override val worksheetStateListMs: List<WorksheetState> get() = wsStateMap.values.toList()

    override val sheetTabBarState: SheetTabBarState
        get() = SheetTabBarStateImp(
            activeSheetPointerMs = activeSheetPointerMs,
            wb = wb
        )

    override var activeSheetPointer: ActiveWorksheetPointer by activeSheetPointerMs

    override fun getWsState(sheetName: String): WorksheetState? {
        return this.getWsStateMs(sheetName)
    }

    override fun getWsState(wsNameSt: St<String>): WorksheetState? {
        return this.getWsStateMs(wsNameSt)
    }

    override fun getWsStateMs(sheetName: String): WorksheetState? {
        val rt = wsStateMap.values.firstOrNull { it.wsName == sheetName }
        return rt
    }

    override fun getWsStateMs(wsNameSt: St<String>): WorksheetState? {
        return wsStateMap[wsNameSt]
    }

    override fun getWsStateMsRs(sheetName: String): Rse<WorksheetState> {
        val w = getWsStateMs(sheetName)
        return w?.let {
            Ok(it)
        }
            ?: Err(WorkbookStateErrors.WorksheetStateNotExist.report("Worksheet state for \"${sheetName}\" does not exist"))
    }

    override fun getWsStateMsRs(wsNameSt: St<String>): Rse<WorksheetState> {
        val w = getWsStateMs(wsNameSt)
        return w?.let {
            Ok(it)
        }
            ?: Err(WorkbookStateErrors.WorksheetStateNotExist.report("Worksheet state for \"${wsNameSt.value}\" does not exist"))
    }

    override fun setActiveSheet(sheetName: String) {
        val ws = this.wb.getWs(sheetName)
        if (ws != null) {
            this.activeSheetPointer = this.activeSheetPointer.pointTo(ws.nameMs)
        }
    }

    override fun refresh() {
        this.refreshWsPointer()
        this.refreshWsState()
    }

    override var wb: Workbook by wbMs

    override val wbKey: WorkbookKey
        get() = wb.key

    override val wbKeyMs: Ms<WorkbookKey>
        get() = wb.keyMs

    /**
     * point the workbook inside this state to a new workbook key, then refresh the state.
     */
    override fun setWorkbookKeyAndRefreshState(newWbKey: WorkbookKey) {
        wb.key = (newWbKey)
        // Assign new active sheet name if need
        val newActiveSheetName = this.pickActiveSheet(wb)
        activeSheetPointer = activeSheetPointer.pointTo(newActiveSheetName)
        this.refreshWsState()
    }

    override fun refreshWsState() {
        var newStateMap: Map<St<String>, WorksheetState> = mutableMapOf()
        val sheetList: List<Ms<Worksheet>> = this.wb.worksheetMsList
        for (wsMs: Ms<Worksheet> in sheetList) {
            val ws: Worksheet = wsMs.value
            val wsState: WorksheetState? = this.getWsStateMs(ws.name)
            if (wsState != null) {
                // x: keep the existing state
                wsState.refreshCellState()
                newStateMap = newStateMap + (wsState.wsNameSt to wsState)
            } else {
                // x: create new state for new sheet
                val newState = this.createDefaultWsState(wsMs)
                newStateMap = newStateMap + (newState.wsNameSt to newState)
            }
        }
        wsStateMapMs.value = newStateMap
    }

    override fun refreshWsPointer() {
        val wb = this.wb
        val currentActiveSheetName = this.activeSheetPointer.wsName
        if (currentActiveSheetName != null) {
            if (wb.containSheet(currentActiveSheetName)) {
            } else {
                val activeSheetName = wb.getWs(0)?.nameMs
                this.activeSheetPointer = this.activeSheetPointer.pointTo(activeSheetName)
            }
        } else {
            val activeSheetName = wb.getWs(0)?.nameMs
            this.activeSheetPointer = this.activeSheetPointer.pointTo(activeSheetName)
        }
    }

    override fun setWbKey(newWbKey: WorkbookKey) {
        wb.key = newWbKey
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

    private fun createDefaultWsState(worksheet: Ms<Worksheet>): WorksheetState {
        val wsMs = worksheet
        val wsState = wsStateFactory.createThenRefresh(
            wsMs = wsMs,
            gridSliderFactory = this.gridSliderFactory,
            cursorStateFactory = this.cursorStateFactory,
            thumbStateFactory = this.thumbStateFactory

        )
        wsState.refreshCellState()
        return wsState
    }


    @P6ExperimentalApi
    @Throws(Exception::class)
    override fun overWriteWb(newWb: Workbook) {
        return this.overWriteWbRs(newWb).getOrThrow()
    }


    @P6ExperimentalApi
    override fun overWriteWbRs(newWb: Workbook): Rse<Unit> {
        if (newWb.key == this.wb.key) {
            this.wb = newWb
            this.refresh()
            return Ok(Unit)
        } else {
            return WorkbookStateErrors.CantOverWriteWorkbook.report("Can't overwrite workbook of this state (${this.wb.key}) because the workbook keys do not match")
                .toErr()
        }
    }

    // TODO fix this
    companion object {
        fun forTesting(
            wbMs: Ms<Workbook>,
            wsStateFactory: WorksheetStateFactory,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
            thumbStateFactory: ThumbStateFactory,
        ): WorkbookStateImp {
            val activeSheetName = wbMs.value.getWs(0)?.nameMs
            return forTesting(
                wbMs = wbMs,
                activeSheetPointerMs = ms(ActiveWorksheetPointerImp(activeSheetName)),
                wsStateFactory = wsStateFactory,
                gridSliderFactory = gridSliderFactory,
                cursorStateFactory = cursorStateFactory,
                thumbStateFactory = thumbStateFactory
            )
        }

        fun forTesting(
            wbMs: Ms<Workbook>,
            activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
            wsStateFactory: WorksheetStateFactory,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
            thumbStateFactory: ThumbStateFactory,
        ): WorkbookStateImp {
            val wsStateMap: Map<St<String>, WorksheetState> = wbMs.value.worksheetMsList
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
                    )
                }.associateBy { it.wsNameSt }

            return WorkbookStateImp(
                wbMs = wbMs,
                activeSheetPointerMs = activeSheetPointerMs,
                wsStateMapMs = ms(wsStateMap),
                wsStateFactory = wsStateFactory,
                gridSliderFactory = gridSliderFactory,
                cursorStateFactory = cursorStateFactory,
                thumbStateFactory = thumbStateFactory,
                windowIdMs = ms(null),
                needSaveMs = ms(false),
            ).apply {
                refresh()
            }
        }
    }

}
