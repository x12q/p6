package com.qxdzbc.p6.app.action.workbook.rename_ws

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetApplier
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport


import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Result
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RenameWorksheetActionImp @Inject constructor(
    val rm:RenameWorksheetRM,
    val applier:RenameWorksheetApplier,
    private val stateContMs:Ms<SubAppStateContainer>
) : RenameWorksheetAction {

    private var sc by stateContMs

    override fun renameWorksheetRs(request: RenameWorksheetRequest): Result<Unit, ErrorReport> {

        val wbStateMs = sc.getWbStateMs(request.wbKey)
        if(wbStateMs!=null){
            val command = object : BaseCommand() {
                val req = request
                val reversedReq = RenameWorksheetRequest(
                    wbKey = req.wbKey,
                    oldName = req.newName,
                    newName = req.oldName
                )

                override fun run() {
                    val res = rm.renameWorksheet(req)
                    if (res != null) {
                        applier.applyResRs(res)
                    }
                }

                override fun undo() {
                    val res = rm.renameWorksheet(reversedReq)
                    if (res != null) {
                        applier.applyResRs(res)
                    }
                }
            }
            sc.getUndoStackMs(WbWs(request.wbKey,request.oldName))?.also{
                it.value = it.value.add(command)
            }
        }

        val o = rm.renameWorksheet(request)
        if (o != null) {
            val applyRs = applier.applyResRs(o)
            return applyRs
        } else {
            return CommonErrors.Unknown.report("Encounter unknown error when trying to rename worksheet ${request.oldName} of workbook ${request.wbKey}")
                .toErr()
        }
    }
}
