package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.common.Rse

/**
 * Encase certain default wb creation code
 */
interface WorkbookFactory {
    fun createWbRs(wbName:String?=null): Rse<Workbook>
}
