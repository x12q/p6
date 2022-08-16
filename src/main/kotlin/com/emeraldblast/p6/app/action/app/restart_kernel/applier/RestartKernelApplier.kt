package com.emeraldblast.p6.app.action.app.restart_kernel.applier

import com.emeraldblast.p6.app.action.app.restart_kernel.RestartKernelResponse

interface RestartKernelApplier {
    fun applyRestartKernel(response: RestartKernelResponse)
}
