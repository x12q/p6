package com.qxdzbc.common

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.error.ErrorReport

object ResultUtils {
    fun <T:Any?> T.toOk(): Ok<T>{
        return Ok(this)
    }

    fun <T,Q:T?> Q.toRs(errorReport: ErrorReport):Rse<T>{
        return this?.let { this.toOk() } ?: errorReport.toErr()
    }
}
