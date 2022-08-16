package com.emeraldblast.p6.rpc.document.workbook.msg

import com.emeraldblast.p6.app.common.err.WithReportNavInfo
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.proto.rpc.workbook.WorkbooKServiceProtos

data class SetWbKeyRequest(
    override val wbKey: WorkbookKey,
    val newWbKey: WorkbookKey,
    override val windowId: String? = null,
) : WithReportNavInfo {
    companion object {
        fun fromProto(proto: WorkbooKServiceProtos.SetWbKeyRequestProto): SetWbKeyRequest {
            return SetWbKeyRequest(
                wbKey = proto.wbKey.toModel(),
                newWbKey = proto.newWbKey.toModel(),
            )
        }
    }
}
