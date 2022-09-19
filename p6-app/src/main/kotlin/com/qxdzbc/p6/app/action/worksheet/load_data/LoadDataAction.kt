package com.qxdzbc.p6.app.action.worksheet.load_data

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.rpc.worksheet.msg.LoadDataRequest

interface LoadDataAction {
    fun loadDataRs(request:LoadDataRequest, publishErrorToUI:Boolean = false):Rse<Unit>
}
