package com.emeraldblast.p6.rpc.document.workbook.msg

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.app.document.worksheet.toModel
import com.emeraldblast.p6.proto.rpc.workbook.WorkbooKServiceProtos.AddWorksheetRequestProto
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.Ms

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
