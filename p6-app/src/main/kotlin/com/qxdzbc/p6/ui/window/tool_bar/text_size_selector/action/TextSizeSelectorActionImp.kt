package com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.tool_bar.return_focus_to_cell.ReturnFocusToCellCursor
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class TextSizeSelectorActionImp @Inject constructor(
    val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
    val returnFocusToCellCursor: ReturnFocusToCellCursor,
    val updateCellFormatAction: UpdateCellFormatAction,
) : TextSizeSelectorAction {

    private val sc by stateContainerSt

    override fun submitManualEdit(windowId: String, value: String) {
        val num = value.toFloatOrNull()
        num?.also {
            updateCellFormatAction.setSelectedCellsTextSize(num)
            sc.getTextSizeSelectorStateMs(windowId)?.also {
                it.value = it.value.setHeaderText(value)
            }
        }
        returnFocusToCellCursor.returnFocusToCurrentCellCursor()
    }

    override fun pickTextSize(windowId: String, textSize: Int) {
        updateCellFormatAction.setSelectedCellsTextSize(textSize.toFloat())
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
