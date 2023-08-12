package com.qxdzbc.p6.err

import com.qxdzbc.common.error.SingleErrorReport

object ErrorContainerUtils {
    fun SingleErrorReport.oddMsg():String{
        return this.toString()
    }
}