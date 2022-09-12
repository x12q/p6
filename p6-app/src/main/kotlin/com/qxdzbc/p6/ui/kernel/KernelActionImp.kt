package com.qxdzbc.p6.ui.kernel


import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelRequest
import com.qxdzbc.p6.app.action.app.AppRM
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplier
import javax.inject.Inject

class KernelActionImp @Inject constructor(
    private val appRM: AppRM,
    private val restartKernelApplier:RestartKernelApplier
): KernelAction {
    override suspend fun startKernelAndServices(pythonExecutablePath: String) {
        val out = appRM.restartKernel(RestartKernelRequest(pythonExecutablePath = pythonExecutablePath))
        restartKernelApplier.applyRestartKernel(out)
    }

    override suspend fun connectToKernelUsingConnectionFilePath(connectionFilePath: String) {
        val out = appRM.restartKernel(RestartKernelRequest(connectionFilePath = connectionFilePath))
        restartKernelApplier.applyRestartKernel(out)
    }

    override suspend fun connectToKernelUsingConnectionFileContent(connectionFileJson: String) {
        val out = appRM.restartKernel(RestartKernelRequest(connectionFileJson  = connectionFileJson))
        restartKernelApplier.applyRestartKernel(out)
    }
}
