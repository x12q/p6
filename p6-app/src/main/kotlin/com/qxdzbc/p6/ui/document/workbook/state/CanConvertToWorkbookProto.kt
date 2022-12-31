package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.p6.proto.DocProtos

interface CanConvertToWorkbookProto{
    fun toProto(): DocProtos.WorkbookProto
}
