package com.qxdzbc.p6.composite_actions.worksheet.delete_cell

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.rpc.communication.res_req_template.response.ResponseWith_WbKey
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.toModel
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.WorksheetProtos.*
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

class DeleteCellResponse(
    override val wbKey: WorkbookKey,
    val wsName:String,
    val cellAddress: CellAddress,
    val newWorkbook: Workbook?,
    override val isError: Boolean,
    override val errorReport: ErrorReport?
): ResponseWith_WbKey {
    companion object{
        fun DeleteCellResponseProto.toModel(translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>): DeleteCellResponse {
            return DeleteCellResponse(
                wbKey = workbookKey.toModel(),
                wsName = worksheetName,
                cellAddress = cellAddress.toModel(),
                newWorkbook = if(this.hasNewWorkbook())newWorkbook.toShallowModel(translatorGetter) else null,
                isError = isError,
                errorReport = if(this.hasErrorReport()) errorReport.toModel() else null
            )
        }
    }
}

