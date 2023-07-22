package com.qxdzbc.p6.app.action.worksheet.ruler.change_col_row_size

import androidx.compose.ui.unit.Dp
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class ChangeRowAndColumnSizeActionImp @Inject constructor(
    private val stateCont:StateContainer
) : ChangeRowAndColumnSizeAction {

    private val sc  = stateCont

    fun makeCommandToChageColWidth(colIndex: Int, sizeDiff: Dp, wbwsSt: WbWsSt): Command {
        val command = object : BaseCommand() {
            val _wbwsSt = wbwsSt
            val _sizeDiff = sizeDiff
            val _colIndex = colIndex
            val _oldWidth = sc.getWsState(_wbwsSt)?.getColumnWidth(_colIndex)

            override fun run() {
                sc.getWsState(_wbwsSt)?.also { wsState ->
                    wsState.changeColWidth(_colIndex, _sizeDiff)
                }
            }

            override fun undo() {
                sc.getWsState(_wbwsSt)?.also { wsState ->
                    if(_oldWidth!=null){
                        wsState.changeColWidth(_colIndex, -_sizeDiff)
                    }else{
                        wsState.restoreColumnWidthToDefault(_colIndex)
                    }
                }
            }
        }
        return command
    }

    override fun changeColWidth(colIndex: Int, sizeDiff: Dp, wbwsSt: WbWsSt, undoable: Boolean) {
        if(undoable){
            sc.getUndoStackMs(wbwsSt)?.also {
                val command = makeCommandToChageColWidth(colIndex, sizeDiff, wbwsSt)
                it.value = it.value.add(command)
            }
        }
        sc.getWsState(wbwsSt)?.also { wsState ->
            wsState.changeColWidth(colIndex, sizeDiff)
        }
    }

    fun makeCommandToChangeRowHeight(rowIndex: Int, sizeDiff: Dp, wbwsSt: WbWsSt): Command {
        val command = object : BaseCommand() {
            val _wbwsSt = wbwsSt
            val _sizeDiff = sizeDiff
            val _rowIndex = rowIndex
            val _oldHeight = sc.getWsState(_wbwsSt)?.getRowHeight(_rowIndex)

            override fun run() {
                sc.getWsState(_wbwsSt)?.also { wsState ->
                    wsState.changeRowHeight(_rowIndex, _sizeDiff)
                }
            }

            override fun undo() {
                sc.getWsState(_wbwsSt)?.also { wsState ->
                    if(_oldHeight!=null){
                        wsState.changeRowHeight(_rowIndex, -_sizeDiff)
                    }else{
                        wsState.restoreRowHeightToDefault(_rowIndex)
                    }
                }
            }
        }
        return command
    }

    override fun changeRowHeight(rowIndex: Int, sizeDiff: Dp, wbwsSt: WbWsSt, undoable: Boolean) {
        if(undoable){
            sc.getUndoStackMs(wbwsSt)?.also {
                val command = makeCommandToChangeRowHeight(rowIndex, sizeDiff, wbwsSt)
                it.value = it.value.add(command)
            }
        }
        
        sc.getWsState(wbwsSt)?.also { wsState ->
            wsState.changeRowHeight(rowIndex, sizeDiff)
        }
    }
}
