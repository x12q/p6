package com.qxdzbc.p6.app.action.app.set_wbkey

import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.ErrorReport

/**
 * Replace workbook key of a workbook with a new workbook key
 */
interface ReplaceWorkbookKeyAction {
    fun replaceWbKey(req: SetWbKeyRequest): Rse<Unit>
}
