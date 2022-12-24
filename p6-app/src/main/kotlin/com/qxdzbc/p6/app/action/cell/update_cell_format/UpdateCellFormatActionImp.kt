package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.flyweight.FlyweightTable
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormatImp
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.CellFormatFlyweightTable
import com.qxdzbc.p6.ui.format.attr.BoolAttr.Companion.toBoolAttr
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(scope = P6AnvilScope::class)
class UpdateCellFormatActionImp @Inject constructor(
    private val cellFormatTableMs: Ms<CellFormatFlyweightTable>,
    private val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
) : UpdateCellFormatAction {

    private val sc by stateContainerSt
    private val cTable by cellFormatTableMs

    override fun setCellBackgroundColor(cellId: CellId, color: Color) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewBackgroundColor(inputState, color)
        applyNewState(cellId, newState)
    }

    private fun makeInputState(cellId: CellId): TargetState {
        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val inputState = TargetState(oldCellState, cTable)
        return inputState
    }

    fun produceNewStateForNewBackgroundColor(
        inputState: TargetState,
        color: Color
    ): TargetState? {
        val newState = produceNewStateWithNewCellFormat(
            inputState = inputState,
            newFormat = color,
            getCurrentFormat = {
                inputState.cellState.cellFormat?.backgroundColor
            },
            getFlyweightTable = {
                inputState.cellFormatTable.colorTable
            },
            produceNewTextFormat = { newColor, oldCellFormat ->
                oldCellFormat.setBackgroundColor(newColor)
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateColorTable(it)
            }
        )
        return newState
    }

    override fun setCellTextSize(cellId: CellId, textSize: Float) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewTextSize(inputState, textSize)
        applyNewState(cellId, newState)
    }

    override fun setCurrentCellTextSize(i: Float) {
        sc.getActiveCursorMs()?.let {
            val cs = it.value
            this.setCellTextSize(cs.mainCellId, i)
        }
    }

    fun produceNewStateForNewTextSize(inputState: TargetState, textSize: Float): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
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

    override fun setCellTextColor(cellId: CellId, color: Color) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewTextColor(inputState, color)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewTextColor(inputState: TargetState, color: Color): TargetState? {
        val oldCellState = inputState.cellState
        val newState = produceNewStateWithNewTextFormat(
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


    override fun setCellTextUnderlined(cellId: CellId, underlined: Boolean) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewUnderlined(inputState, underlined)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewUnderlined(inputState: TargetState, underlined: Boolean): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
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


    override fun setCellTextCrossed(cellId: CellId, crossed: Boolean) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewCrossed(inputState, crossed)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewCrossed(inputState: TargetState, crossed: Boolean): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
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

    override fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewHorizontalAlignment(inputState, alignment)
        applyNewState(cellId, newState)
    }

    override fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewVerticalAlignment(inputState, alignment)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewVerticalAlignment(
        inputState: TargetState,
        alignment: TextVerticalAlignment
    ): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
            cellState = inputState.cellState,
            newFormat = alignment,
            getCurrentFormat = {
                it?.verticalAlignment
            },
            produceNewTextFormat = { newAlignment, oldFormat ->
                oldFormat.setVerticalAlignment(newAlignment)
            },
            getFlyweightTable = {
                inputState.cellFormatTable.verticalAlignmentTable
            },
            produceNewCellFormatTable = {
                inputState.cellFormatTable.updateVerticalAlignmentTable(it)
            }
        )
        return newState
    }

    fun produceNewStateForNewHorizontalAlignment(
        inputState: TargetState,
        alignment: TextHorizontalAlignment
    ): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
            cellState = inputState.cellState,
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

    fun <T> produceNewStateWithNewTextFormat(
        cellState: CellState,
        newFormat: T,
        getCurrentFormat: (TextFormat?) -> T?,
        getFlyweightTable: () -> FlyweightTable<T>,
        produceNewTextFormat: (T, TextFormat) -> TextFormat,
        produceNewCellFormatTable: (FlyweightTable<T>) -> CellFormatFlyweightTable,
    ): TargetState? {
        return produceNewState(
            newFormatValue = newFormat,
            getCurrentFormatObj = {
                cellState.textFormat ?: TextFormat.createDefaultTextFormat()
            },
            getCurrentFormat = { getCurrentFormat(cellState.textFormat) },
            getFlyweightTable = getFlyweightTable,
            produceNewFormatObj = produceNewTextFormat,
            produceNewCellState = {
                cellState.setTextFormat(it)
            },
            produceNewCellFormatTable = produceNewCellFormatTable,
        )
    }

    fun <T> produceNewStateWithNewCellFormat(
        inputState: TargetState,
        newFormat: T,
        getCurrentFormat: (CellFormat?) -> T?,
        getFlyweightTable: () -> FlyweightTable<T>,
        produceNewTextFormat: (T, CellFormat) -> CellFormat,
        produceNewCellFormatTable: (FlyweightTable<T>) -> CellFormatFlyweightTable,
    ): TargetState? {
        val rt = produceNewState(
            newFormatValue = newFormat,
            getCurrentFormatObj = { inputState.cellState.cellFormat ?: CellFormatImp() },
            getCurrentFormat = { getCurrentFormat(inputState.cellState.cellFormat) },
            getFlyweightTable = getFlyweightTable,
            produceNewFormatObj = produceNewTextFormat,
            produceNewCellState = {
                inputState.cellState.setCellFormat(it)
            },
            produceNewCellFormatTable = produceNewCellFormatTable,
        )
        return rt
    }

    fun <T, F> produceNewState(
        newFormatValue: T,
        getCurrentFormatObj: () -> F,
        getCurrentFormat: () -> T?,
        getFlyweightTable: () -> FlyweightTable<T>,
        produceNewFormatObj: (T, F) -> F,
        produceNewCellState: (F) -> CellState,
        produceNewCellFormatTable: (FlyweightTable<T>) -> CellFormatFlyweightTable,
    ): TargetState? {
        val oldFormatValue = getCurrentFormat()
        if (oldFormatValue != newFormatValue) {
            val formatTable = getFlyweightTable()
            var newCellTable: CellFormatFlyweightTable
            val ft2 = formatTable.addOrUpdate(newFormatValue)
            val oldFormatObj = getCurrentFormatObj()
            newCellTable = produceNewCellFormatTable(ft2)
            val newCellFormat = produceNewFormatObj(newFormatValue, oldFormatObj)
            val newCellState = produceNewCellState(newCellFormat)
            // x: clean up old format attr
            if (oldFormatValue != null) {
                newCellTable = produceNewCellFormatTable(ft2.reduceCount(oldFormatValue))
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
        val cellFormatTable: CellFormatFlyweightTable
    )
}
