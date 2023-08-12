package com.qxdzbc.p6.document_data_layer.workbook

import com.qxdzbc.common.Rse

/**
 * Encase certain default wb creation code
 */
interface WorkbookFactory {
    fun createWbRs(wbName:String?=null): Rse<Workbook>
}
