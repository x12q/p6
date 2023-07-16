package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.p6.proto.DocProtos

/**
 * for Workbook and WorkbookState
 */
interface CanConvertToWorkbookProto{
    fun toProto(): DocProtos.WorkbookProto
}
