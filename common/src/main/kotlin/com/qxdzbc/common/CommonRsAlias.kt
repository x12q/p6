package com.qxdzbc.common

import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result

typealias Rs<T,E> = Result<T, E>
typealias Rse<T> = Result<T, ErrorReport>
