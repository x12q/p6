package com.qxdzbc.p6.ui.window.menu.action


import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.ui.app.action.AppAction
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.state.app_state.MsKernelContextQualifier
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CodeMenuActionImp @Inject constructor(
    private val windowAction: WindowAction,
    private val appAction: AppAction,
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
        throw UnsupportedOperationException()
    }
}
