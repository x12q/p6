package com.qxdzbc.p6.composite_actions.app.set_wbkey

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.WorkbookProtos

data class SetWbKeyRequest(
    override val wbKey: WorkbookKey,
    val newWbKey: WorkbookKey,
    override val windowId: String? = null,
) : com.qxdzbc.p6.common.err.WithReportNavInfo {
    companion object {
        fun fromProto(proto: WorkbookProtos.SetWbKeyRequestProto): SetWbKeyRequest {
            return SetWbKeyRequest(
                wbKey = proto.wbKey.toModel(),
                newWbKey = proto.newWbKey.toModel(),
            )
        }
    }
}
