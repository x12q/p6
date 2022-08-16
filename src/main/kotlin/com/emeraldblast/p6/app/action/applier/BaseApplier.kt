package com.emeraldblast.p6.app.action.applier

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.communication.res_req_template.response.Response
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok

interface BaseApplier:BaseApplier2 {
    fun <T : Response> applyRes(res: T?, onResOk: (res: T) -> Unit)
    fun <T : Response> applyResRs(res: T?):Rs<T,ErrorReport>
    fun <T> applyResRsNoNav(res:RseNav<T>):Rs<T,ErrorReport>
}


interface BaseApplier2{
    fun <T> applyResRsKeepNav(res:RseNav<T>):RseNav<T>
}
