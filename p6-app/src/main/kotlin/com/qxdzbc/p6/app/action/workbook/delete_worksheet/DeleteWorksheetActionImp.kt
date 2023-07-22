package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import com.github.michaelbull.result.*
import com.qxdzbc.common.ResultUtils.toOk


import com.qxdzbc.p6.rpc.workbook.WorkbookRpcMsgErrors
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteWorksheetActionImp @Inject constructor(
    private val docCont: DocumentContainer,
    private val stateCont: StateContainer,
    val transContMs: TranslatorContainer,
) : DeleteWorksheetAction {

    private val dc = docCont

    override fun deleteWorksheetRs(request: WorksheetIdWithIndexPrt): Rse<Unit> {
        if (request.wsName != null || request.wsIndex != null) {
            val res = makeRequest(request)
            when (res) {
                is Ok -> {
                    if (request.wsName != null) {
                        return applyResRs(request.wsName, res)
                    } else {
                        return dc.getWbRs(request.wbKey)
                            .andThen { wb ->
                                wb.getWsRs(request.wsIndex!!)
                            }.andThen { ws ->
                                val wsName = ws.name
                                applyResRs(wsName, res)
                            }
                    }
                }

                is Err -> {
                    // do nothing
                    return res
                }
            }

        } else {
            return WorkbookRpcMsgErrors.IllegalMsg.report("IdentifyWorksheetMsg must contain at least a worksheet name or a worksheet index")
                .toErr()
        }
    }

    private var tc = transContMs

    fun applyResRs(deletedWsName: String, rs: Rse<Workbook>): Rse<Unit> {
        return rs.flatMap { newWB ->
            // x: update wb
            docCont.replaceWb(newWB)
            // x: update wb state
            val wbKey = newWB.key
            val wbStateMs = stateCont.getWbState(wbKey)
            wbStateMs?.refresh()
            wbStateMs?.needSave = true
            //update translator map
            tc.removeTranslator(wbKey, deletedWsName)
            Unit.toOk()
        }
    }

    fun makeRequest(request: WorksheetIdWithIndexPrt): Rse<Workbook> {
        val wbKey = request.wbKey
        val name = request.wsName
        val index = request.wsIndex
        if (name != null) {
            return this.deleteWorksheetRs(wbKey, name)
        } else if (index != null) {
            return this.deleteWorksheetRs(wbKey, index)
        } else {
            return WorkbookRpcMsgErrors.IllegalMsg
                .report("IdentifyWorksheetMsg must contain at least a worksheet name or a worksheet index")
                .toErr()
        }
    }

    private fun deleteWorksheetRs(wbKey: WorkbookKey, wsName: String): Rse<Workbook> {
        val wbRs = dc.getWbRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsName).map {
                wb.reRun()
                wb
            }
        }
        return rt
    }

    private fun deleteWorksheetRs(wbKey: WorkbookKey, wsIndex: Int): Rse<Workbook> {
        val wbRs = dc.getWbRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsIndex).map {
                wb.reRun()
                wb
            }
        }
        return rt
    }
}
