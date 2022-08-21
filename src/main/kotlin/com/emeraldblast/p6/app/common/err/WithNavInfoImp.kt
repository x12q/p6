package com.emeraldblast.p6.app.common.err

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

class WithNavInfoImp(
    override val wbKey: WorkbookKey?,
    override val windowId: String?,
) : WithReportNavInfo
