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
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormatImp
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

    override fun setCellBackgroundColor(cellId: CellId, color: Color?) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewBackgroundColor(cellId,inputState, color)
        applyNewState(cellId, newState)
    }

    private fun makeInputState(cellId: CellId): TargetState {
//        val oldCellState = sc.getCellState(cellId) ?: CellStates.blank(cellId.address)
        val oldCellState = sc.getCellState(cellId)
        val inputState = TargetState(oldCellState, cTable)
        return inputState
    }

    fun produceNewStateForNewBackgroundColor(
        cellId: CellId,
        inputState: TargetState,
        color: Color?
    ): TargetState? {
        val newState = produceNewStateWithNewCellFormat(
            cellId=cellId,
            inputState = inputState,
            newFormatValue = color,
            getCurrentFormatValue = {
                inputState.cellState?.cellFormat?.backgroundColor
            },
            getFlyweightTable = {
                inputState.cellFormatTable.colorTable
            },
            produceNewCellFormat = { newColor, oldCellFormat ->
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
        val newState = produceNewStateForNewTextSize(cellId,inputState, textSize)
        applyNewState(cellId, newState)
    }

    override fun setCurrentCellTextSize(i: Float) {
        sc.getActiveCursorStateMs()?.let {
            val cs = it.value
            this.setCellTextSize(cs.mainCellId, i)
        }
    }

    fun produceNewStateForNewTextSize(cellId: CellId,inputState: TargetState, textSize: Float): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
            cellId=cellId,
            cellState = inputState.cellState,
            newFormatValue = textSize,
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

    override fun setCellTextColor(cellId: CellId, color: Color?) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewTextColor(cellId, inputState, color)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewTextColor(cellId: CellId,inputState: TargetState, color: Color?): TargetState? {
        val oldCellState = inputState.cellState
        val newState = produceNewStateWithNewTextFormat(
            cellId=cellId,
            cellState = oldCellState,
            newFormatValue = color,
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
        val newState = produceNewStateForNewUnderlined(cellId,inputState, underlined)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewUnderlined(cellId: CellId,inputState: TargetState, underlined: Boolean): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
            cellId=cellId,
            cellState = inputState.cellState,
            newFormatValue = underlined.toBoolAttr(),
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
        val newState = produceNewStateForNewCrossed(cellId,inputState, crossed)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewCrossed(cellId: CellId,inputState: TargetState, crossed: Boolean): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
            cellId=cellId,
            cellState = inputState.cellState,
            newFormatValue = crossed.toBoolAttr(),
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
        val newState = produceNewStateForNewHorizontalAlignment(cellId,inputState, alignment)
        applyNewState(cellId, newState)
    }

    override fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment) {
        val inputState = makeInputState(cellId)
        val newState = produceNewStateForNewVerticalAlignment(cellId,inputState, alignment)
        applyNewState(cellId, newState)
    }

    fun produceNewStateForNewVerticalAlignment(
        cellId: CellId,
        inputState: TargetState,
        alignment: TextVerticalAlignment
    ): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
            cellId=cellId,
            cellState = inputState.cellState,
            newFormatValue = alignment,
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
        cellId: CellId,
        inputState: TargetState,
        alignment: TextHorizontalAlignment?
    ): TargetState? {
        val newState = produceNewStateWithNewTextFormat(
            cellId = cellId,
            cellState = inputState.cellState,
            newFormatValue = alignment,
            getCurrentFormat = {
                it?.horizontalAlignment
            },
            produceNewTextFormat = { newFormatValue, currentFormatObj ->
                currentFormatObj.setHorizontalAlignment(newFormatValue)
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
        cellId: CellId,
        cellState: CellState?,
        newFormatValue: T?,
        getCurrentFormat: (TextFormat?) -> T?,
        getFlyweightTable: () -> FlyweightTable<T>,
        produceNewTextFormat: (T?, TextFormat) -> TextFormat,
        produceNewCellFormatTable: (FlyweightTable<T>) -> CellFormatFlyweightTable,
    ): TargetState? {
        return produceNewState(
            newFormatValue = newFormatValue,
            getCurrentFormatObj = {
                cellState?.textFormat ?: TextFormat.createDefaultTextFormat()
            },
            getCurrentFormatValue = {  getCurrentFormat(cellState?.textFormat) } ,
            getFlyweightTable = getFlyweightTable,
            produceNewFormatObj = { _newFormatValue, currentFormatObj ->
                if (_newFormatValue != null) {
                    val baseFormat = currentFormatObj ?: TextFormatImp()
                    produceNewTextFormat(_newFormatValue, baseFormat)
                } else {
                    currentFormatObj?.let {
                        produceNewTextFormat(_newFormatValue, it)
                    }
                }
            },
            produceNewCellState = { newCellFormat ->
                val newCellState = cellState?.setTextFormat(newCellFormat)
                    ?: if (newCellFormat != null) {
                        CellStates.blank(cellId.address).setTextFormat(newCellFormat)
                    } else {
                        null
                    }
                newCellState
            },
            produceNewCellFormatTable = produceNewCellFormatTable,
        )
    }

    fun <T> produceNewStateWithNewCellFormat(
        cellId: CellId,
        inputState: TargetState,
        newFormatValue: T?,
        getCurrentFormatValue: (CellFormat) -> T?,
        getFlyweightTable: () -> FlyweightTable<T>,
        produceNewCellFormat: (T?, CellFormat) -> CellFormat,
        produceNewCellFormatTable: (FlyweightTable<T>) -> CellFormatFlyweightTable,
    ): TargetState? {
        val rt = produceNewState(
            newFormatValue = newFormatValue,
            getCurrentFormatObj = { inputState.cellState?.cellFormat ?: CellFormatImp() },
            getCurrentFormatValue = { inputState.cellState?.cellFormat?.let { getCurrentFormatValue(it) } },
            getFlyweightTable = getFlyweightTable,
            produceNewFormatObj = { _newFormatValue, currentFormatObj ->
                if (_newFormatValue != null) {
                    val baseFormat = currentFormatObj ?: CellFormatImp()
                    produceNewCellFormat(_newFormatValue, baseFormat)
                } else {
                    currentFormatObj?.let {
                        produceNewCellFormat(_newFormatValue, it)
                    }
                }
            },
            produceNewCellState = { newCellFormat ->
                val newCellState = inputState.cellState?.setCellFormat(newCellFormat)
                    ?: if (newCellFormat != null) {
                        CellStates.blank(cellId.address).setCellFormat(newCellFormat)
                    } else {
                        null
                    }
                newCellState
            },
            produceNewCellFormatTable = produceNewCellFormatTable,
        )
        return rt
    }

    fun <T, F> produceNewState(
        newFormatValue: T?,
        getCurrentFormatObj: () -> F?,
        getCurrentFormatValue: () -> T?,
        getFlyweightTable: () -> FlyweightTable<T>,
        produceNewFormatObj: (T?, F?) -> F?,
        produceNewCellState: (F?) -> CellState?,
        produceNewCellFormatTable: (FlyweightTable<T>) -> CellFormatFlyweightTable,
    ): TargetState? {
        val currentFormatValue = getCurrentFormatValue()
        if (currentFormatValue != newFormatValue) {
            // x: produce new cell state
            val newCellState: CellState? = run {
                val currentFormatObj: F? = getCurrentFormatObj()
                val newFormatObj: F? = produceNewFormatObj(newFormatValue, currentFormatObj)
                produceNewCellState(newFormatObj)
            }

            // x: update format table with new format value
            val currentFt = getFlyweightTable()
            val newFt: FlyweightTable<T> = newFormatValue?.let { currentFt.addOrUpdate(it) } ?: currentFt
            var newCellTable: CellFormatFlyweightTable = produceNewCellFormatTable(newFt)

            // x: clean up old format attr
            if (currentFormatValue != null) {
                newCellTable = produceNewCellFormatTable(newFt.reduceCount(currentFormatValue))
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
        targetState?.cellState?.also {
            sc.getWsStateMs(cellId)?.also { wsStateMs ->
                val newWsState = wsStateMs.value.addOrOverwriteCellState(it)
                wsStateMs.value = newWsState
            }
        }
        targetState?.cellFormatTable?.also {
            cellFormatTableMs.value = it
        }
    }

    data class TargetState(
        val cellState: CellState?,
        val cellFormatTable: CellFormatFlyweightTable
    )
}
