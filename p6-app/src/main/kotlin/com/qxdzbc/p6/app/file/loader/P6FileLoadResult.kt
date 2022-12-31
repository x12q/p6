package com.qxdzbc.p6.app.file.loader

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.proto.DocProtos

data class P6FileLoadResult(
    val workbook: Workbook,
    val workbookProto: DocProtos.WorkbookProto
)
