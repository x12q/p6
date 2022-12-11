package com.qxdzbc.p6.app.action.cell.update_cell_format

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
import com.qxdzbc.common.flyweight.FlyweightTable
import com.qxdzbc.p6.ui.format.attr.BoolAttr.Companion.toBoolAttr
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
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewTextSize(TargetState(oldCellState,cTable),textSize)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewTextSize(inputState: TargetState, textSize: Float): TargetState? {
        val newState = produceNewState(
            cellState = inputState.cellState,
            newFormat = textSize,
            getCurrentFormat = {
                it?.textSize
            },
            produceNewTextFormat = { newTextSize, oldFormat ->
                oldFormat.setTextSize(newTextSize)
            },
            getFlyweightTable = {
                inputState.cellFormatTable.floatTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateFloatTable(it)
            }
        )
        return newState
    }

    override fun setTextColor(cellId: CellId, color: Color) {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewTextColor(TargetState(oldCellState, cTable), color)
        applyNewState(cellId, newState)
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
            getFlyweightTable = {
                inputState.cellFormatTable.colorTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateColorTable(it)
            }
        )
        return newState
    }


    override fun setUnderlined(cellId: CellId, underlined: Boolean) {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewUnderlined(TargetState(oldCellState, cTable), underlined)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewUnderlined(inputState: TargetState, underlined: Boolean): TargetState? {
        val newState = produceNewState(
            cellState = inputState.cellState,
            newFormat = underlined.toBoolAttr(),
            getCurrentFormat = {
                it?.isUnderlinedAttr
            },
            produceNewTextFormat = { newUnderlined, oldFormat ->
                oldFormat.setUnderlinedAttr(newUnderlined)
            },
            getFlyweightTable = {
                inputState.cellFormatTable.boolTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateBoolTable(it)
            }
        )
        return newState
    }


    override fun setCrossed(cellId: CellId, crossed: Boolean) {
        val cellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewCrossed(TargetState(cellState, cTable), crossed)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewCrossed(inputState: TargetState, crossed: Boolean): TargetState? {
        val newState = produceNewState(
            cellState = inputState.cellState,
            newFormat = crossed.toBoolAttr(),
            getCurrentFormat = {
                it?.isCrossedAttr
            },
            produceNewTextFormat = { newCrossed, oldFormat ->
                oldFormat.setCrossedAttr(newCrossed)
            },
            getFlyweightTable = {
                inputState.cellFormatTable.boolTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateBoolTable(it)
            },
        )
        return newState
    }

    override fun setHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment) {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val newState = produceNewStateForNewHorizontalAlignment(TargetState(oldCellState, cTable), alignment)
        applyNewState(cellId, newState)
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
            getFlyweightTable = {
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
        getFlyweightTable: () -> FlyweightTable<T>,
        produceNewTextFormat: (T, TextFormat) -> TextFormat,
        produceNewCellFormatTable: (FlyweightTable<T>) -> CellFormatTable,
    ): TargetState? {
        val oldFormat = getCurrentFormat(cellState.textFormat)
        if (oldFormat != newFormat) {
            val formatTable = getFlyweightTable()
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
