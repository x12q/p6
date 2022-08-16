package com.emeraldblast.p6.app.coderunner

import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FakeCodeRunner @Inject constructor() : CodeRunner {
    override suspend fun run(code: String, dispatcher: CoroutineDispatcher): Result<String, ErrorReport> {
        return Ok(code)
    }
}
