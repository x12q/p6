package com.qxdzbc.p6.ui.format.action

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.FormatTable
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(scope = P6AnvilScope::class)
class UpdateCellFormatActionImp @Inject constructor(
    private val cellFormatTableMs: Ms<CellFormatTable>,
    private val stateContainerSt: St<@JvmSuppressWildcards StateContainer>
) : UpdateCellFormatAction {

    private val sc by stateContainerSt
    private val cTable by cellFormatTableMs
    private val fTable get() = cTable.floatTable
    private val colorTable get() = cTable.colorTable

    override fun setTextSize(cellId: CellId, textSize: Float) {
        setFormat(
            cellId = cellId,
            newFormat = textSize,
            getCurrentFormat = {
                it?.textSize
            },
            updateFormat = { newTextSize, oldFormat ->
                oldFormat.setTextSizeAttr(newTextSize)
            },
            getFormatTable = {
                cTable.floatTable
            },
            updateCellTable = {
                cellFormatTableMs.value = cellFormatTableMs.value.updateFloatTable(it)
            }
        )
    }

    override fun setTextColor(cellId: CellId, color: Color) {
        setFormat(
            cellId = cellId,
            newFormat = color,
            getCurrentFormat = {
                it?.textColor
            },
            updateFormat = { newColor, oldFormat ->
                oldFormat.setTextColor(newColor)
            },
            getFormatTable = {
                cTable.colorTable
            },
            updateCellTable = {
                cellFormatTableMs.value = cellFormatTableMs.value.updateColorTable(it)
            }
        )
    }

    fun <T> setFormat(
        cellId: CellId,
        newFormat: T,
        getCurrentFormat: (TextFormat?) -> T?,
        updateFormat: (T, TextFormat) -> TextFormat,
        getFormatTable: () -> FormatTable<T>,
        updateCellTable: (newTable: FormatTable<T>) -> Unit,
    ) {
        getCellStateMsOrCreateNew(cellId)?.also { cellStateMs ->
            val cellState by cellStateMs
            val oldFormat = getCurrentFormat(cellState.textFormat)
            val formatTable = getFormatTable()

            if (oldFormat != newFormat) {
                val (ft2, _) = formatTable.add(newFormat)
                updateCellTable(ft2)
                val cellFormatMs = cellState.textFormatMs
                val newFormat2 = updateFormat(newFormat, (cellFormatMs.value ?: TextFormat.createDefaultTextFormat()))
                cellFormatMs.value = newFormat2
                // x: clean up old format attr
                if (oldFormat != null) {
                    updateCellTable(ft2.reduceCountIfPossible(oldFormat))
                }
            }
        }
    }

    /**
     * check if the respective cell state object exists,
     * if not, create a new one in the target worksheet.
     */
    private fun getCellStateMsOrCreateNew(cellId: CellId): Ms<CellState>? {
        val cellStateMs = sc.getCellStateMs(cellId)
            ?: run {
                sc.getWsStateMs(cellId)?.let { wsStateMs ->
                    wsStateMs.value = wsStateMs.value.addBlankCellState(cellId.address)
                    wsStateMs.value.getCellStateMs(cellId.address)
                }
            }
        return cellStateMs
    }
}
