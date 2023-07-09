package com.qxdzbc.p6.app.document.wb_container

import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import java.nio.file.Path

abstract class AbsWorkbookContainer : WorkbookContainer {

    override val size: Int
        get() = allWbs.size

    override fun getWbRs(wbKeySt: St<WorkbookKey>): Rse<Workbook> {
        return getWbMsRs(wbKeySt).map { it.value }
    }

    override fun getWb(wbKeySt: St<WorkbookKey>): Workbook? {
        return getWbMs(wbKeySt)?.component1()
    }

    override fun getWbMs(wbKeySt: St<WorkbookKey>): Ms<Workbook>? {
        return getWbMsRs(wbKeySt).component1()
    }
    @Throws(Exception::class)
    override fun addWb(wb: Workbook) {
        return this.addWbRs(wb).getOrThrow()
    }

    override fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>? {
        return this.getWbMsRs(wbKey).component1()
    }

    override fun getWb(wbKey: WorkbookKey): Workbook? {
        val rs = this.getWbRs(wbKey)
        return rs.component1()
    }

    override fun getWbRs(wbKey: WorkbookKey): Rse<Workbook> {
        return this.getWbMsRs(wbKey).map { it.value }
    }

    override fun getWb(path: Path): Workbook? {
        return this.getWbRs(path).component1()
    }

    override fun getWbRs(path: Path):Rse<Workbook>{
        return this.getWbMsRs(path).map { it.value }
    }
}
