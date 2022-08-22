package com.qxdzbc.p6.app.app_context

import com.qxdzbc.p6.app.code.PythonCommander
import com.qxdzbc.p6.app.coderunner.CodeRunner
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext

/**
 */
interface AppContext {
    val username:String
    val kernelContext:KernelContext
    val codeRunner:CodeRunner
    val backendCommander:PythonCommander
}
