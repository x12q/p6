package com.qxdzbc.p6.ui.document.worksheet.cursor.actions

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardAction
import com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event.HandleCursorKeyboardEventAction
import com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor.PasteRangeToCursor
import com.qxdzbc.p6.app.action.cursor.undo_on_cursor.UndoOnCursorAction
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action.ThumbAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType = CursorAction::class)
class CursorActionImp @Inject constructor(
    private val wsAction: WorksheetAction,
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val formulaColorGenerator: FormulaColorGenerator,
    private val pasteRangeToCursor: PasteRangeToCursor,
    private val selectWholeCol: SelectWholeColumnForAllSelectedCellAction,
    private val selectWholeRow: SelectWholeRowForAllSelectedCellAction,
    override val cellEditorAction: CellEditorAction,
    override val thumbAction: ThumbAction,
    private val handleCursorKeyboardEventAction: HandleCursorKeyboardEventAction,
    private val copyCursorRangeToClipboardAction: CopyCursorRangeToClipboardAction,
    private val undoOnCursorAct: UndoOnCursorAction,
) : CursorAction,
    SelectWholeColumnForAllSelectedCellAction by selectWholeCol,
    SelectWholeRowForAllSelectedCellAction by selectWholeRow,
    HandleCursorKeyboardEventAction by handleCursorKeyboardEventAction,
    PasteRangeToCursor by pasteRangeToCursor,
    CopyCursorRangeToClipboardAction by copyCursorRangeToClipboardAction,
    UndoOnCursorAction by undoOnCursorAct {
    private val sc by stateContSt

    /**
     * Get a map of [RangeAddress] and color from the cell which the cell cursor is currently at
     * @param wbws where the cursor is locating
     */
    override fun getFormulaRangeAndColor(wbws: WbWs): Map<RangeAddress, Color> {
        val ces by sc.cellEditorStateMs
        if (ces.isOpen) {

            // this read on the range info from the target cell
//            val targetCell: Cell? = ces.targetCell?.let {
//                sc.getCellOrDefault(wbws.wbKey,wbws.wsName,it)
//            }
//            val ranges = targetCell?.content?.exUnit?.getRangeIds()?: emptyList()
//            val colors = formulaColorGenerator.getColors(ranges.size)
//            val colorMap:Map<RangeAddress, Color> = buildMap {
//                for((i,rid) in ranges.withIndex()){
//                    if(rid.wbKey == wbws.wbKey && rid.wsName == wbws.wsName){
//                        put(rid.rangeAddress,colors[i])
//                    }
//                }
//            }
//
            val ranges = ces.displayTextElementResult?.cellRangeElements?.mapNotNull {
                RangeAddresses.fromLabelRs(it.cellRangeLabel).component1()
            } ?: emptyList()
            val colors = formulaColorGenerator.getColors(ranges.size)
            val colorMap: Map<RangeAddress, Color> = buildMap {
                for ((i, rid) in ranges.withIndex()) {
                    put(rid, colors[i])
                }
            }
            return colorMap
        } else {
            return emptyMap()
        }
    }

    override fun focusOnCursor(cursorId: CursorStateId) {
        sc.getFocusStateMsByWbKeyRs(cursorId.wbKey).onSuccess {
            it.value = it.value.focusOnCursor()
        }
    }

    override fun freeFocusOnCursor(cursorId: CursorStateId) {
        sc.getFocusStateMsByWbKeyRs(cursorId.wbKey).onSuccess {
            it.value = it.value.freeFocusOnCursor()
        }
    }

    override fun updateCursorFocus(cursorId: CursorStateId, focused: Boolean) {
        sc.getFocusStateMsByWbKeyRs(cursorId.wbKey).onSuccess {
            it.value = it.value.setCursorFocus(focused)
        }
    }

    override fun moveCursorTo(wbws: WbWs, cellLabel: String) {
        sc.getCursorStateMs(wbws)?.also { cursorStateMs ->
            cursorStateMs.value = cursorStateMs.value.setMainCell(CellAddress(cellLabel))
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, wbws)
        }
    }
}


