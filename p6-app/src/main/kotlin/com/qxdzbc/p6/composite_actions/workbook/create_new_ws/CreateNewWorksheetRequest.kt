package com.qxdzbc.p6.composite_actions.workbook.create_new_ws

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.document_data_layer.worksheet.WorksheetImp.Companion.toShallowModel
import com.qxdzbc.p6.proto.WorkbookProtos
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

data class CreateNewWorksheetRequest(
    val wbKey: WorkbookKey,
    val worksheet: Worksheet,
    val windowId: String? = null
) {
    companion object {
        fun WorkbookProtos.AddWorksheetRequestProto.toModel(
            wbKeyMs: Ms<WorkbookKey>,
            translator: P6Translator<ExUnit>
        ): CreateNewWorksheetRequest {
            val wbk = this.wbKey.toModel()
            return CreateNewWorksheetRequest(
                wbKey = wbk,
                worksheet = this.worksheet.toShallowModel(wbKeyMs, translator)
            )
        }
    }
}
