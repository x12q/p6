package com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action

import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.tool_bar.return_focus_to_cell.ReturnFocusToCellCursor
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ContributesBinding(P6AnvilScope::class)
class TextSizeSelectorActionImp @Inject constructor(
    val stateContainerSt: StateContainer,
    val returnFocusToCellCursor: ReturnFocusToCellCursor,
    val updateCellFormatAction: UpdateCellFormatAction,
) : TextSizeSelectorAction {

    private val sc = stateContainerSt

    override fun submitManualEdit(windowId: String, value: String) {
        val num = value.toFloatOrNull()
        num?.also {
            updateCellFormatAction.setSelectedCellsTextSize(num,undoable=true)
            sc.getTextSizeSelectorStateMs(windowId)?.also {
                it.value = it.value.setHeaderText(value)
            }
        }
        returnFocusToCellCursor.returnFocusToCurrentCellCursor()
    }

    override fun pickTextSize(windowId: String, textSize: Int) {
        updateCellFormatAction.setSelectedCellsTextSize(textSize.toFloat(),undoable=true)
        sc.getTextSizeSelectorStateMs(windowId)?.also {
            it.value = it.value.setHeaderText(textSize.toString())
        }
        returnFocusToCellCursor.returnFocusToCurrentCellCursor()
    }

    override fun setHeaderTextOfTextSizeSelector(windowId: String, newText: String) {
        sc.getTextSizeSelectorStateMs(windowId)?.also {
            it.value = it.value.setHeaderText(newText)
        }
    }
}
