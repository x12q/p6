package com.qxdzbc.p6.app.action.app.restart_kernel.rm

import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelRequest
import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelResponse

interface RestartKernelRM {
    suspend fun restartKernel(request: RestartKernelRequest): RestartKernelResponse
}
