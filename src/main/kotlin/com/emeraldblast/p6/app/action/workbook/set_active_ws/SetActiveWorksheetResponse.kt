package com.emeraldblast.p6.app.action.workbook.set_active_ws

import com.emeraldblast.p6.app.common.proto.toModel
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.AppEventProtos
import com.google.protobuf.ByteString

class SetActiveWorksheetResponse(
    override val wbKey: WorkbookKey,
    val wsName: String,
    override val isError:Boolean = false,
    override val errorReport: ErrorReport? = null
) :ResponseWithWorkbookKeyTemplate{
    companion object{
        fun fromProtoBytes(bytes:ByteString):SetActiveWorksheetResponse{
            return AppEventProtos.SetActiveWorksheetResponseProto.newBuilder().mergeFrom(bytes).build().toModel()
        }
        fun AppEventProtos.SetActiveWorksheetResponseProto.toModel():SetActiveWorksheetResponse{
            return SetActiveWorksheetResponse(
                wbKey=workbookKey.toModel(),
                wsName = worksheetName,
                isError=isError,
                errorReport = if(this.hasErrorReport()){
                    this.errorReport.toModel()
                }else{
                    null
                }
            )
        }
    }

    override fun isLegal(): Boolean {
        return true
    }
}


