package com.emeraldblast.p6.app.coderunner

import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * A simplify interface that accepts a piece of Python code, execute it, then return a representative String.
 * Depend on implementation, the representative String may carry different meaning.
 * Code text provided to [CodeRunner] is assumed to be in correct form, so implementations of this interface do not need to worry about that.
 */
interface CodeRunner {
    /**
     * run a piece of cell code, return a representative String
     */
    suspend fun run(code:String, dispatcher:CoroutineDispatcher = Dispatchers.IO):Result<String,ErrorReport>
}
