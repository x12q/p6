package com.qxdzbc.p6.ui.kernel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.state.app_state.KernelStatusQualifier
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelConfig
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelStatus
import com.qxdzbc.p6.message.api.message.protocol.KernelConnectionFileContent
import com.qxdzbc.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Result
import java.nio.file.Path
import javax.inject.Inject

/**
 * The purpose of this kernel context is that it can influence a Ms of [KernelStatus] on its event
 */
class MsKernelContext @Inject constructor(
    private val kernelContext: KernelContext,
    @KernelStatusQualifier
    private val kernelStatusStateMs: Ms<KernelStatus>
) : KernelContext by kernelContext {
    var kernelStatusState by kernelStatusStateMs
    private fun u(){
        kernelStatusState = kernelContext.kernelStatus
    }
    override fun restartKernel(): Result<Unit, ErrorReport> {
        val rs = kernelContext.restartKernel()
        u()
        return rs
    }

    override fun restartKernel(kernelConfig: KernelConfig): Result<Unit, ErrorReport> {
        val rs = kernelContext.restartKernel(kernelConfig)
        u()
        return rs
    }

    override fun restartKernel(connectionFileContent: KernelConnectionFileContent): Result<Unit, ErrorReport> {
        val rs = kernelContext.restartKernel(connectionFileContent)
        u()
        return rs
    }

    override fun restartKernel(connectionFilePath: Path): Result<Unit, ErrorReport> {
        val rs = kernelContext.restartKernel(connectionFilePath)
        u()
        return rs
    }

    override fun stopAll(): Result<Unit, ErrorReport> {
        val rs = kernelContext.stopAll()
        u()
        return rs
    }

    override fun stopKernel(): Result<Unit, ErrorReport> {
        val rs = kernelContext.stopKernel()
        u()
        return rs
    }

    override fun setConnectionFileContent(connectionFileContent: KernelConnectionFileContent): KernelContext {
        val rs = kernelContext.setConnectionFileContent(connectionFileContent)
        u()
        return rs
    }

    override fun setConnectionFilePath(connectionFilePath: Path): KernelContext {
        val rs = kernelContext.setConnectionFilePath(connectionFilePath)
        u()
        return rs
    }

    override fun setKernelConfig(kernelConfig: KernelConfig): KernelContext {
        val rs = kernelContext.setKernelConfig(kernelConfig)
        u()
        return rs
    }

    override fun startAll(): Result<Unit, ErrorReport> {
        val rs = kernelContext.startAll()
        u()
        return rs
    }

    override fun startKernel(): Result<Unit, ErrorReport> {
        val rs = kernelContext.startKernel()
        u()
        return rs
    }

    override fun startKernel(kernelConfig: KernelConfig): Result<Unit, ErrorReport> {
        val rs = kernelContext.startKernel(kernelConfig)
        u()
        return rs
    }

    override fun startKernel(connectionFileContent: KernelConnectionFileContent): Result<Unit, ErrorReport> {
        val rs = kernelContext.startKernel(connectionFileContent)
        u()
        return rs
    }

    override fun startKernel(connectionFilePath: Path): Result<Unit, ErrorReport> {
        val rs = kernelContext.startKernel(connectionFilePath)
        u()
        return rs
    }
}
