package com.qxdzbc.p6.common.utils

import com.qxdzbc.p6.common.err.ErrorReportWithNavInfo
import com.github.michaelbull.result.Result

typealias RseNav<T> = Result<T, com.qxdzbc.p6.common.err.ErrorReportWithNavInfo>
