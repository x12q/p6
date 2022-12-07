package com.qxdzbc.p6.ui.format.action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import com.qxdzbc.p6.ui.format.text_attr.FontSize
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
    private val fTable get()=cTable.floatValueFormatTable


    override fun setTextSize(cellId: CellId, textSize: Float) {
        val cellStateMs = sc.getCellStateMs(cellId)
            ?: run {
                sc.getWsStateMs(cellId)?.let { wsStateMs ->
                    wsStateMs.value = wsStateMs.value.addBlankCellState(cellId.address)
                    wsStateMs.value.getCellStateMs(cellId.address)
                }
            }
        cellStateMs?.also {
            val cellState by it
            // x: decrease old attr
            val oldTextSize = cellState.textFormat3.textSizeMs.value.attr.attrValue

            // x: increase current or new attr
            val newOrCurrentAttr = fTable.getFloatMarkedAttr(textSize)?.apply {
                this.value = this.value.upCounter()
            } ?: run {
                val newAttr = MarkedAttributes.wrap(FontSize(textSize)).upCounter().toMs()

                cellFormatTableMs.value = cellFormatTableMs.value.updateFloatFormatTable(
                    fTable.addFloatMarkedAttr(textSize, newAttr)
                )
                newAttr
            }
            val cellFormatMs = cellStateMs.value.textFormat3Ms
            val newFormat = cellFormatMs.value.setTextSizeAttr(newOrCurrentAttr)
            cellFormatMs.value = newFormat

            // x: clean up old format attr
            fTable.getFloatMarkedAttr(oldTextSize)?.also { attrMs->
                attrMs.value = attrMs.value.downCounter()
                if(attrMs.value.isCounterZero){
                    cellFormatTableMs.value = cellFormatTableMs.value.updateFloatFormatTable(
                        fTable.removeFloatMarkedAttr(oldTextSize)
                    )
                }
            }
        }
    }
}
