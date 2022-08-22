package com.qxdzbc.p6.app.code

import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

/**
 * Contain functions to control the python side
 */
interface PythonCommander {
    suspend fun initBackEnd(): Result<String, ErrorReport>
}

