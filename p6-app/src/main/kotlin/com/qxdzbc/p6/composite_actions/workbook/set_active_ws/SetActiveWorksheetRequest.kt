package com.qxdzbc.p6.composite_actions.workbook.set_active_ws

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.AppProtos

class SetActiveWorksheetWithIndexRequest(
    val wbKey: WorkbookKey,
    val wsIndex: Int,
)

class SetActiveWorksheetRequest(
    override val wbKey: WorkbookKey,
    override val wsName: String,
) : RequestWithWorkbookKey,WbWs {
//    override fun toProtoBytes(): ByteString {
//        return this.toProto().toByteString()
//    }
    companion object{
        fun AppProtos.SetActiveWorksheetRequestProto.toModel():SetActiveWorksheetRequest{
            return SetActiveWorksheetRequest(
                wbKey = workbookKey.toModel(),
                wsName = worksheetName
            )
        }

    }
    fun toProto(): AppProtos.SetActiveWorksheetRequestProto {
        val rt = AppProtos.SetActiveWorksheetRequestProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setWorksheetName(wsName)
            .build()
        return rt
    }
}

