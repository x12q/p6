package com.qxdzbc.p6.app.common.utils

import com.github.michaelbull.result.Ok

object ResultUtils {
    fun <T:Any?> T.toOk(): Ok<T>{
        return Ok(this)
    }
}
