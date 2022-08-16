package com.emeraldblast.p6.ui.window.status_bar.kernel_status

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.di.state.app_state.KernelStatusQualifier
import com.emeraldblast.p6.di.state.app_state.MsKernelContextQualifier
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelStatus
import com.emeraldblast.p6.message.api.message.protocol.KernelConnectionFileContent
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.map
import javax.inject.Inject

data class KernelStatusItemStateImp @Inject constructor(
    @com.emeraldblast.p6.di.False
    override val detailIsShown: Boolean,
    @MsKernelContextQualifier
    val kernelContext: KernelContext,
    @KernelStatusQualifier
    val kernelStatusMs:Ms<KernelStatus>
) : KernelStatusItemState {
    val kernelStatus by kernelStatusMs
    override fun showDetail(): KernelStatusItemState {
        return this.copy(detailIsShown = true)
    }

    override fun hideDetail(): KernelStatusItemState {
        return this.copy(detailIsShown = false)
    }

    private val connectionFileContent: KernelConnectionFileContent?
        get() = kernelContext.getConnectionFileContent().component1()

    override val kernelIsRunning: Boolean
        get() = kernelStatus.isRunning()
    override val kernelIsUnderManagement: Boolean
        get() = kernelStatus.isProcessUnderManagement
    override val kernelCommand: String?
        get() = kernelContext.kernelConfig?.makeCompleteLaunchCmd()?.joinToString(" ")
    override val connectionFilePath: String?
        get() = kernelContext.getConnectionFilePath()?.toString()
    override val kernelProcessId: Long?
        get() = kernelContext.getKernelProcess().map { it.pid() }.component1()
    override val shellPort: Int?
        get() = connectionFileContent?.shellPort
    override val ioPubPort: Int?
        get() = connectionFileContent?.iopubPort
    override val stdinPort: Int?
        get() = connectionFileContent?.stdinPort
    override val heartBeatPort: Int?
        get() = connectionFileContent?.heartBeatPort
    override val ip: String?
        get() = connectionFileContent?.ip
}
