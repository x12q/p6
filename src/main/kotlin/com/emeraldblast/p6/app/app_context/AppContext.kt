package com.emeraldblast.p6.app.app_context

import com.emeraldblast.p6.app.code.PythonCommander
import com.emeraldblast.p6.app.coderunner.CodeRunner
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext

/**
 */
interface AppContext {
    val username:String
    val kernelContext:KernelContext
    val codeRunner:CodeRunner
    val backendCommander:PythonCommander
}
