package com.emeraldblast.p6.app.document.wb_container

import com.emeraldblast.p6.app.common.utils.ErrorUtils.getOrThrow
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import java.nio.file.Path

abstract class BaseWorkbookContainer : WorkbookContainer {
    override fun addWb(wb: Workbook): WorkbookContainer {
        return this.addWbRs(wb).getOrThrow()
    }
    override fun getWb(wbKey: WorkbookKey): Workbook? {
        val rs = this.getWbRs(wbKey)
        return rs.component1()
    }

    override fun getWb(path: Path): Workbook? {
        return this.getWbRs(path).component1()
    }
}
