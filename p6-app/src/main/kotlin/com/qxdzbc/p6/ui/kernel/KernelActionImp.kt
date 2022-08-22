package com.qxdzbc.p6.ui.kernel

import com.qxdzbc.p6.app.action.app.AppApplier
import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelRequest
import com.qxdzbc.p6.app.action.app.AppRM
import javax.inject.Inject

class KernelActionImp @Inject constructor(
    private val appRM: AppRM,
    private val appApplier: AppApplier
): KernelAction {
    override suspend fun startKernelAndServices(pythonExecutablePath: String) {
        val out = appRM.restartKernel(RestartKernelRequest(pythonExecutablePath = pythonExecutablePath))
        appApplier.applyRestartKernel(out)
    }

    override suspend fun connectToKernelUsingConnectionFilePath(connectionFilePath: String) {
        val out = appRM.restartKernel(RestartKernelRequest(connectionFilePath = connectionFilePath))
        appApplier.applyRestartKernel(out)
    }

    override suspend fun connectToKernelUsingConnectionFileContent(connectionFileJson: String) {
        val out = appRM.restartKernel(RestartKernelRequest(connectionFileJson  = connectionFileJson))
        appApplier.applyRestartKernel(out)
    }
}
