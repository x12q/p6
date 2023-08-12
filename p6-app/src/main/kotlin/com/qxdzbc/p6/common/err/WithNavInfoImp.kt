package com.qxdzbc.p6.common.err

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

class WithNavInfoImp(
    override val wbKey: WorkbookKey?,
    override val windowId: String?,
) : com.qxdzbc.p6.common.err.WithReportNavInfo
