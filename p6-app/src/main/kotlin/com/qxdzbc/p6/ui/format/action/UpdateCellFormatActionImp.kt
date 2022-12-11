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
        val newState = produceNewStateForNewTextSize(cellId, textSize)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewTextSize(cellId: CellId, textSize: Float): TargetState? {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewState(
            cellState = oldCellState,
            newFormat = textSize,
            getCurrentFormat = {
                it?.textSize
            },
            produceNewTextFormat = { newTextSize, oldFormat ->
                oldFormat.setTextSize(newTextSize)
            },
            getFormatTable = {
                cTable.floatTable
            },
            produceNewCellFormatTable = {
                cellFormatTableMs.value.updateFloatTable(it)
            }
        )
        return newState
    }

    override fun setTextColor(cellId: CellId, color: Color) {
        val newState = produceNewStateForNewTextColor(cellId, color)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewTextColor(cellId: CellId, color: Color): TargetState? {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewTextColor(TargetState(oldCellState, cTable), color)
        return newState
    }

    fun produceNewStateForNewTextColor(inputState: TargetState, color: Color): TargetState? {
        val oldCellState = inputState.cellState
        val newState = produceNewState(
            cellState = oldCellState,
            newFormat = color,
            getCurrentFormat = {
                it?.textColor
            },
            produceNewTextFormat = { newColor, oldFormat ->
                oldFormat.setTextColor(newColor)
            },
            getFormatTable = {
                inputState.cellFormatTable.colorTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateColorTable(it)
            }
        )
        return newState
    }


    override fun setUnderlined(cellId: CellId, underlined: Boolean) {
        val newState = produceNewStateForNewUnderlined(cellId, underlined)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewUnderlined(cellId: CellId, underlined: Boolean): TargetState? {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewState(
            cellState = oldCellState,
            newFormat = underlined,
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
        return newState
    }


    override fun setCrossed(cellId: CellId, crossed: Boolean) {
        val newState = produceNewStateForNewCrossed(cellId, crossed)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewCrossed(cellId: CellId, crossed: Boolean): TargetState? {
        val cellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewCrossed(TargetState(cellState, cTable), crossed)
        return newState
    }

    fun produceNewStateForNewCrossed(inputState: TargetState, crossed: Boolean): TargetState? {
        val newState = produceNewState(
            cellState = inputState.cellState,
            newFormat = crossed,
            getCurrentFormat = {
                it?.isCrossed
            },
            produceNewTextFormat = { newCrossed, oldFormat ->
                oldFormat.setCrossed(newCrossed)
            },
            getFormatTable = {
                inputState.cellFormatTable.boolTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateBoolTable(it)
            },
        )
        return newState
    }

    override fun setHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment) {
        val newState = produceNewStateForNewHorizontalAlignment(cellId, alignment)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewHorizontalAlignment(
        cellId: CellId,
        alignment: TextHorizontalAlignment
    ): TargetState? {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewHorizontalAlignment(TargetState(oldCellState, cTable), alignment)
        return newState
    }

    fun produceNewStateForNewHorizontalAlignment(
        inputState: TargetState,
        alignment: TextHorizontalAlignment
    ): TargetState? {
        val oldCellState = inputState.cellState
        val newState = produceNewState(
            cellState = oldCellState,
            newFormat = alignment,
            getCurrentFormat = {
                it?.horizontalAlignment
            },
            produceNewTextFormat = { newColor, oldFormat ->
                oldFormat.setHorizontalAlignment(newColor)
            },
            getFormatTable = {
                inputState.cellFormatTable.horizontalAlignmentTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateHorizontalAlignmentTable(it)
            }
        )
        return newState
    }

    fun <T> produceNewState(
        cellState: CellState,
        newFormat: T,
        getCurrentFormat: (TextFormat?) -> T?,
        getFormatTable: () -> FormatTable<T>,
        produceNewTextFormat: (T, TextFormat) -> TextFormat,
        produceNewCellFormatTable: (FormatTable<T>) -> CellFormatTable,
    ): TargetState? {
        val oldFormat = getCurrentFormat(cellState.textFormat)
        if (oldFormat != newFormat) {
            val formatTable = getFormatTable()
            var newCellTable: CellFormatTable
            val ft2 = formatTable.addOrUpdate(newFormat)
            newCellTable = produceNewCellFormatTable(ft2)
            val cellFormat = cellState.textFormat
            val newTextFormat: TextFormat =
                produceNewTextFormat(newFormat, (cellFormat ?: TextFormat.createDefaultTextFormat()))
            val newCellState = cellState.setTextFormat(newTextFormat)
            // x: clean up old format attr
            if (oldFormat != null) {
                newCellTable = produceNewCellFormatTable(ft2.reduceCountIfPossible(oldFormat))
            }
            return TargetState(
                newCellState,
                newCellTable
            )
        } else {
            return null
        }
    }

    /**
     * Apply the new state to the app state
     */
    fun applyNewState(
        cellId: CellId,
        targetState: TargetState?,
    ) {
        targetState?.also {
            sc.getWsStateMs(cellId)?.also { wsStateMs ->
                val newWsState = wsStateMs.value.addOrOverwriteCellState(targetState.cellState)
                wsStateMs.value = newWsState
            }
            cellFormatTableMs.value = targetState.cellFormatTable
        }
    }

    data class TargetState(
        val cellState: CellState,
        val cellFormatTable: CellFormatTable
    )
}
