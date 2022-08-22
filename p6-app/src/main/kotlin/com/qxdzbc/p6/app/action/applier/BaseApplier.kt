package com.qxdzbc.p6.app.action.applier

import com.qxdzbc.p6.app.common.utils.Rs
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.communication.res_req_template.response.Response
import com.qxdzbc.p6.common.exception.error.ErrorReport

interface BaseApplier:BaseApplier2 {
    fun <T : Response> applyRes(res: T?, onResOk: (res: T) -> Unit)
    fun <T : Response> applyResRs(res: T?): Rs<T, ErrorReport>
    fun <T> applyResRsNoNav(res: RseNav<T>): Rs<T, ErrorReport>
}


interface BaseApplier2{
    fun <T> applyResRsKeepNav(res: RseNav<T>): RseNav<T>
}
