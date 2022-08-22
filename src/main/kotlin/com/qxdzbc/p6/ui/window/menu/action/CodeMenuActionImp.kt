package com.qxdzbc.p6.ui.window.menu.action

import com.qxdzbc.p6.di.state.app_state.MsKernelContextQualifier
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.ui.app.action.AppAction
import com.qxdzbc.p6.app.action.window.WindowAction
import javax.inject.Inject

class CodeMenuActionImp @Inject constructor(
    private val windowAction: WindowAction,
    private val appAction: AppAction,
    @MsKernelContextQualifier
    private val kernelContext: KernelContext,
) : CodeMenuAction {
    override fun openCodeEditor() {
        appAction.openCodeEditor()
    }

    override fun openDialogToStartKernel(windowId:String) {
        windowAction.openDialogToStartKernel(windowId)
    }

    override fun openDialogToConnectToKernel(windowId:String) {
        windowAction.openDialogToConnectToKernel(windowId)
    }

    override fun stopKernel() {
        kernelContext.stopKernel()
    }
}
