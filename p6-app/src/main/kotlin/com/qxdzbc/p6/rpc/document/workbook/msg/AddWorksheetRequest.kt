package com.qxdzbc.p6.rpc.document.workbook.msg

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.toModel
import com.qxdzbc.p6.proto.rpc.workbook.WorkbooKServiceProtos.AddWorksheetRequestProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.Ms

data class AddWorksheetRequest(
    val wbKey:WorkbookKey,
    val worksheet:Worksheet,
    val windowId:String? = null
) {
    companion object{
        fun AddWorksheetRequestProto.toModel(wbKeyMs:Ms<WorkbookKey>,translator: P6Translator<ExUnit>):AddWorksheetRequest{
            val wbk = this.wbKey.toModel()
            return AddWorksheetRequest(
                wbKey = wbk,
                worksheet = this.worksheet.toModel(wbKeyMs,translator)
            )
        }
    }
}
