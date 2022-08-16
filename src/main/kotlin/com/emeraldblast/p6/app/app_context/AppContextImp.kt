package com.emeraldblast.p6.app.app_context

import com.emeraldblast.p6.app.code.BackEndCommander
import com.emeraldblast.p6.app.coderunner.CodeRunner
import com.emeraldblast.p6.di.Username
import com.emeraldblast.p6.di.state.app_state.MsKernelContextQualifier
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import javax.inject.Inject


class AppContextImp @Inject constructor(
    @Username
    override val username: String,
    @MsKernelContextQualifier
    override val kernelContext: KernelContext,
    override val codeRunner: CodeRunner,
    override val backendCommander: BackEndCommander,
) : AppContext {
}
