package com.qxdzbc.p6.app.action.cell.copy_cell

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.IndCellImp
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject

class CopyCellActionImp @Inject constructor(
    @StateContainerSt
    val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : CopyCellAction {
    private val stateCont by stateContSt
    override fun copyCell(request: CopyCellRequest): Rse<Unit> {
        val fMs = stateCont.getCellMs(request.fromCell)
        val tMs: Ms<Cell>? = stateCont.getCellMs(request.toCell)
        if (fMs != null) {
            if (fMs.value.content.isNotEmpty()) {
                if (tMs == null) {
                    // x: destination cell does not exist, create it if possible
                    val getWsRs:Rse<Ms<Worksheet>> = stateCont.getWsMsRs(request.toCell)
                    when (getWsRs) {
                        is Ok -> {
                            val wsMs:Ms<Worksheet> = getWsRs.value
                            val wsStateMs:Ms<WorksheetState>? = stateCont.getWsStateMs(request.toCell)
                            val newWs:Worksheet = wsMs.value.addOrOverwrite(IndCellImp(request.toCell.address))
                            wsMs.value = newWs
                            val tms2Rs: Rse<Ms<Cell>> = newWs.getCellMsRs(request.toCell.address)
                            when (tms2Rs) {
                                is Ok -> {
                                    val tms2:Ms<Cell> = tms2Rs.value
                                    tms2.value = tms2.value.setContent(fMs.value.content)
                                    wsStateMs?.let {
                                        it.value = it.value.refresh()
                                    }
                                    return Ok(Unit)
                                }
                                is Err -> {
                                    return tms2Rs
                                }
                            }
                        }
                        is Err -> {
                            return getWsRs
                        }
                    }
                } else {
                    tMs.value = tMs.value.setContent(fMs.value.content)
                }
            } else {
                if (tMs != null) {
                    if (tMs.value.content.isNotEmpty()) {
                        tMs.value = tMs.value.setContent(fMs.value.content)
                    }
                }
            }
            return Ok(Unit)
        } else {
            if (tMs != null) {
                if (tMs.value.content.isNotEmpty()) {
                    tMs.value = tMs.value.setContent(CellContentImp.empty)
                }
            }
            return Ok(Unit)
        }
    }
}
