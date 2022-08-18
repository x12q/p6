package com.emeraldblast.p6.app.action.workbook.rename_ws

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.command.BaseCommand
import com.emeraldblast.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetApplier
import com.emeraldblast.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.emeraldblast.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.di.state.app_state.AppStateMs

import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Result
import javax.inject.Inject

class RenameWorksheetActionImp @Inject constructor(

    val rm:RenameWorksheetRM,
    val applier:RenameWorksheetApplier,
    @AppStateMs
    val appStateMs:Ms<AppState>
) : RenameWorksheetAction {

    private var appState by appStateMs

    override fun renameWorksheetRs(request: RenameWorksheetRequest): Result<Unit, ErrorReport> {

        val wbStateMs = appState.getWbStateMs(request.wbKey)
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
            wbStateMs.value = wbStateMs.value.addCommand(command)
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
