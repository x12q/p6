package com.qxdzbc.p6.rpc.document.workbook.msg

import com.qxdzbc.p6.app.common.err.WithReportNavInfo
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.WorkbookProtos

data class SetWbKeyRequest(
    override val wbKey: WorkbookKey,
    val newWbKey: WorkbookKey,
    override val windowId: String? = null,
) : WithReportNavInfo {
    companion object {
        fun fromProto(proto: WorkbookProtos.SetWbKeyRequestProto): SetWbKeyRequest {
            return SetWbKeyRequest(
                wbKey = proto.wbKey.toModel(),
                newWbKey = proto.newWbKey.toModel(),
            )
        }
    }
}
