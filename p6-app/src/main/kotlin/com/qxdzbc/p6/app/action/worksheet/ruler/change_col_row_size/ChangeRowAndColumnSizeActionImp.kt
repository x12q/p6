package com.qxdzbc.p6.app.action.worksheet.ruler.change_col_row_size

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ChangeRowAndColumnSizeActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : ChangeRowAndColumnSizeAction {

    private val sc by stateContSt

    fun makeCommandToChageColWidth(colIndex: Int, sizeDiff: Int, wbwsSt: WbWsSt): Command {
        val command = object : BaseCommand() {
            val _wbwsSt = wbwsSt
            val _sizeDiff = sizeDiff
            val _colIndex = colIndex
            val _oldWidth = sc.getWsState(_wbwsSt)?.getColumnWidth(_colIndex)

            override fun run() {
                sc.getWsStateMs(_wbwsSt)?.also { wsStateMs ->
                    wsStateMs.value = wsStateMs.value.changeColWidth(_colIndex, _sizeDiff)
                }
            }

            override fun undo() {
                sc.getWsStateMs(_wbwsSt)?.also { wsStateMs ->
                    if(_oldWidth!=null){
                        wsStateMs.value = wsStateMs.value.changeColWidth(_colIndex, -_sizeDiff)
                    }else{
                        wsStateMs.value = wsStateMs.value.restoreColumnWidthToDefault(_colIndex)
                    }
                }
            }
        }
        return command
    }

    override fun changeColWidth(colIndex: Int, sizeDiff: Int, wbwsSt: WbWsSt, undoable: Boolean) {
        if(undoable){
            sc.getUndoStackMs(wbwsSt)?.also {
                val command = makeCommandToChageColWidth(colIndex, sizeDiff, wbwsSt)
                it.value = it.value.add(command)
            }
        }
        sc.getWsStateMs(wbwsSt)?.also { wsStateMs ->
            wsStateMs.value = wsStateMs.value.changeColWidth(colIndex, sizeDiff)
        }
    }

    fun makeCommandToChangeRowHeight(rowIndex: Int, sizeDiff: Int, wbwsSt: WbWsSt): Command {
        val command = object : BaseCommand() {
            val _wbwsSt = wbwsSt
            val _sizeDiff = sizeDiff
            val _rowIndex = rowIndex
            val _oldHeight = sc.getWsState(_wbwsSt)?.getRowHeight(_rowIndex)

            override fun run() {
                sc.getWsStateMs(_wbwsSt)?.also { wsStateMs ->
                    wsStateMs.value = wsStateMs.value.changeRowHeight(_rowIndex, _sizeDiff)
                }
            }

            override fun undo() {
                sc.getWsStateMs(_wbwsSt)?.also { wsStateMs ->
                    if(_oldHeight!=null){
                        wsStateMs.value = wsStateMs.value.changeRowHeight(_rowIndex, -_sizeDiff)
                    }else{
                        wsStateMs.value = wsStateMs.value.restoreRowHeightToDefault(_rowIndex)
                    }
                }
            }
        }
        return command
    }

    override fun changeRowHeight(rowIndex: Int, sizeDiff: Int, wbwsSt: WbWsSt, undoable: Boolean) {
        if(undoable){
            sc.getUndoStackMs(wbwsSt)?.also {
                val command = makeCommandToChangeRowHeight(rowIndex, sizeDiff, wbwsSt)
                it.value = it.value.add(command)
            }
        }


        sc.getWsStateMs(wbwsSt)?.also { wsStateMs ->
            val newWsState = wsStateMs.value.changeRowHeight(rowIndex, sizeDiff)
            wsStateMs.value = newWsState
        }
    }
}
