package com.qxdzbc.p6.file.loader

import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.proto.DocProtos

data class P6FileLoadResult(
    val workbook: Workbook,
    val workbookProto: DocProtos.WorkbookProto
)
