package com.qxdzbc.p6.ui.format.action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.cell_format.TextFormat3
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
    private val fTable get() = cTable.floatValueFormatTable


    override fun setTextSize(cellId: CellId, textSize: Float) {
        // x: check if the respective cell state object exists,
        // x: if not, create a new one
        val cellStateMs = sc.getCellStateMs(cellId)
            ?: run {
                sc.getWsStateMs(cellId)?.let { wsStateMs ->
                    wsStateMs.value = wsStateMs.value.addBlankCellState(cellId.address)
                    wsStateMs.value.getCellStateMs(cellId.address)
                }
            }
        cellStateMs?.also {
            val cellState by it
            val oldTextSize = cellState.textFormat3?.textSizeMs?.value?.attr?.attrValue
            if (oldTextSize != textSize) {
                val (t2, newAttrMs) = fTable.add(textSize)
                cellFormatTableMs.value = cellFormatTableMs.value.updateFloatFormatTable(t2)
                val cellFormatMs = cellState.textFormat3Ms
                val newFormat = (cellFormatMs.value ?: TextFormat3.defaultCellFormat).setTextSizeAttr(newAttrMs)
                cellFormatMs.value = newFormat
                // x: clean up old format attr
                if(oldTextSize!=null){
                    cellFormatTableMs.value = cellFormatTableMs.value.updateFloatFormatTable(
                        fTable.reduceCountIfPossible(oldTextSize)
                    )
                }
            }
        }
    }
}
