package com.qxdzbc.common

import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport

typealias Rs<T,E> = Result<T, E>
typealias Rse<T> = Result<T, ErrorReport>
