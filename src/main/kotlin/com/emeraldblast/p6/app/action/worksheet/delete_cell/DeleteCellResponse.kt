package com.emeraldblast.p6.app.action.worksheet.delete_cell

import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.common.proto.toModel
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.address.toModel
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookImp.Companion.toModel
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.WorksheetProtos.*
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString

class DeleteCellResponse(
    override val wbKey: WorkbookKey,
    val wsName:String,
    val cellAddress: CellAddress,
    val newWorkbook: Workbook?,
    override val isError: Boolean,
    override val errorReport: ErrorReport?
):ResponseWithWorkbookKeyTemplate{
    companion object {
        fun fromProtoBytes(
            data:ByteString,
            translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>): DeleteCellResponse {
            return DeleteCellResponseProto.newBuilder().mergeFrom(data).build().toModel(translatorGetter)
        }
    }

    override fun isLegal(): Boolean {
        return true
    }
}

fun DeleteCellResponseProto.toModel(translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>): DeleteCellResponse {
    return DeleteCellResponse(
        wbKey = workbookKey.toModel(),
        wsName = worksheetName,
        cellAddress = cellAddress.toModel(),
        newWorkbook = if(this.hasNewWorkbook())newWorkbook.toModel(translatorGetter) else null,
        isError = isError,
        errorReport = if(this.hasErrorReport()) errorReport.toModel() else null
    )
}
