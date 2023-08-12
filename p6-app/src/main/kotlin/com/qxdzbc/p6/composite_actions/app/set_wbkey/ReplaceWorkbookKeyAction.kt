package com.qxdzbc.p6.composite_actions.app.set_wbkey

import com.qxdzbc.common.Rse

/**
 * Replace workbook key of a workbook with a new workbook key
 */
interface ReplaceWorkbookKeyAction {
    fun replaceWbKey(req: SetWbKeyRequest): Rse<Unit>
}
