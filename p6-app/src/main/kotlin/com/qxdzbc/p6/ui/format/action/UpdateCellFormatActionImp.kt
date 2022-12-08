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
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.FormatTable
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

    override fun setTextSize(cellId: CellId, textSize: Float) {
        val oldCellState = sc.getCellState(cellId)
        val newState = produceNewState(
            cellId = cellId,
            cellState=oldCellState,
            newFormat=textSize,
            getCurrentFormat = {
                it?.textSize
            },
            produceNewTextFormat = { newTextSize, oldFormat ->
                oldFormat.setTextSizeAttr(newTextSize)
            },
            getFormatTable = {
                cTable.floatTable
            },
            produceNewCellFormatTable = {
                cellFormatTableMs.value.updateFloatTable(it)
            }
        )
        updateState(cellId,newState)
    }

    override fun setTextColor(cellId: CellId, color: Color) {
        val oldCellState = sc.getCellState(cellId)
        val newState = produceNewState(
            cellId = cellId,
            cellState=oldCellState,
            newFormat=color,
            getCurrentFormat = {
                it?.textColor
            },
            produceNewTextFormat = { newColor, oldFormat ->
                oldFormat.setTextColor(newColor)
            },
            getFormatTable = {
                cTable.colorTable
            },
            produceNewCellFormatTable = {
                cellFormatTableMs.value.updateColorTable(it)
            }
        )
        updateState(cellId,newState)
    }

    override fun setUnderlined(cellId: CellId, underlined: Boolean) {
        val oldCellState = sc.getCellState(cellId)
        val newState = produceNewState(
            cellId = cellId,
            cellState=oldCellState,
            newFormat=underlined,
            getCurrentFormat = {
                it?.isUnderlined
            },
            produceNewTextFormat = { newUnderlined, oldFormat ->
                oldFormat.setUnderlined(newUnderlined)
            },
            getFormatTable = {
                cTable.boolTable
            },
            produceNewCellFormatTable = {
                cellFormatTableMs.value.updateBoolTable(it)
            }
        )
        updateState(cellId,newState)
    }

    override fun setCrossed(cellId: CellId, crossed: Boolean) {
        val oldCellState = sc.getCellState(cellId)
        val newState = produceNewState(
            cellId = cellId,
            cellState=oldCellState,
            newFormat=crossed,
            getCurrentFormat = {
                it?.isCrossed
            },
            produceNewTextFormat = { newCrossed, oldFormat ->
                oldFormat.setCrossed(newCrossed)
            },
            getFormatTable = {
                cTable.boolTable
            },
            produceNewCellFormatTable = {
                cellFormatTableMs.value.updateBoolTable(it)
            },
        )
        updateState(cellId,newState)
    }

    override fun setHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment) {
        val oldCellState = sc.getCellState(cellId)
        val newState = produceNewState(
            cellId = cellId,
            cellState=oldCellState,
            newFormat=alignment,
            getCurrentFormat = {
                it?.horizontalAlignment
            },
            produceNewTextFormat = { newColor, oldFormat ->
                oldFormat.setHorizontalAlignment(newColor)
            },
            getFormatTable = {
                cTable.horizontalAlignmentTable
            },
            produceNewCellFormatTable = {
                cellFormatTableMs.value.updateHorizontalAlignmentTable(it)
            }
        )
        updateState(cellId,newState)
    }

    fun <T> produceNewState(
        cellId: CellId,
        cellState: CellState?,
        newFormat: T,
        getCurrentFormat: (TextFormat?) -> T?,
        getFormatTable: () -> FormatTable<T>,
        produceNewTextFormat: (T, TextFormat) -> TextFormat,
        produceNewCellFormatTable: (FormatTable<T>) -> CellFormatTable,
    ): NewState? {
        val cs = cellState?: CellStates.blank(cellId.address)
        val oldFormat = getCurrentFormat(cs.textFormat)
        val formatTable = getFormatTable()
        var newCellTable: CellFormatTable
        if (oldFormat != newFormat) {
            val (ft2, _) = formatTable.add(newFormat)
            newCellTable=produceNewCellFormatTable(ft2)
            val cellFormat = cs.textFormat
            val newTextFormat: TextFormat =
                produceNewTextFormat(newFormat, (cellFormat ?: TextFormat.createDefaultTextFormat()))
            val newCellState = cs.setTextFormat(newTextFormat)
            // x: clean up old format attr
            if (oldFormat != null) {
                newCellTable = produceNewCellFormatTable(ft2.reduceCountIfPossible(oldFormat))
            }
            return NewState(
                newCellState,
                newCellTable
            )
        } else {
            return null
        }
    }

    fun updateState(
        cellId: CellId,
        newState:NewState?,
    ) {
        newState?.also {
            cellFormatTableMs.value = it.newCellFormatTable
            getCellStateMsOrCreateNew(cellId)?.also { cellStateMs ->
                cellStateMs.value = it.newCellState
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

    class NewState(
        val newCellState: CellState,
        val newCellFormatTable: CellFormatTable
    )
}
