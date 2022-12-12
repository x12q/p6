package com.qxdzbc.p6.ui.kernel


import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelRequest
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplier
import com.qxdzbc.p6.app.action.app.restart_kernel.rm.RestartKernelRM
import javax.inject.Inject

class KernelActionImp @Inject constructor(
    private val restartKernelRM: RestartKernelRM,
    private val restartKernelApplier:RestartKernelApplier
): KernelAction {
    override suspend fun startKernelAndServices(pythonExecutablePath: String) {
        val out = restartKernelRM.restartKernel(RestartKernelRequest(pythonExecutablePath = pythonExecutablePath))
        restartKernelApplier.applyRestartKernel(out)
    }

    override suspend fun connectToKernelUsingConnectionFilePath(connectionFilePath: String) {
        val out = restartKernelRM.restartKernel(RestartKernelRequest(connectionFilePath = connectionFilePath))
        restartKernelApplier.applyRestartKernel(out)
    }

    override suspend fun connectToKernelUsingConnectionFileContent(connectionFileJson: String) {
        val out = restartKernelRM.restartKernel(RestartKernelRequest(connectionFileJson  = connectionFileJson))
        restartKernelApplier.applyRestartKernel(out)
    }
}
