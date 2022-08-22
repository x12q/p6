package com.qxdzbc.p6.app.app_context

import com.qxdzbc.p6.app.code.PythonCommander
import com.qxdzbc.p6.app.coderunner.CodeRunner
import com.qxdzbc.p6.di.Username
import com.qxdzbc.p6.di.state.app_state.MsKernelContextQualifier
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import javax.inject.Inject


class AppContextImp @Inject constructor(
    @Username
    override val username: String,
    @MsKernelContextQualifier
    override val kernelContext: KernelContext,
    override val codeRunner: CodeRunner,
    override val backendCommander: PythonCommander,
) : AppContext {
}
