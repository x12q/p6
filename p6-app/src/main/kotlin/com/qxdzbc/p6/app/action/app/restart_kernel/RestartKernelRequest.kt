package com.qxdzbc.p6.app.action.app.restart_kernel

import com.qxdzbc.p6.app.communication.res_req_template.IsLegal
import com.qxdzbc.p6.app.communication.res_req_template.request.local.LocalRequestWithWindowId

data class RestartKernelRequest(
    val connectionFileJson: String? = null,
    val connectionFilePath: String? = null,
    val pythonExecutablePath: String? = null,
    override val windowId: String? = null,
) : LocalRequestWithWindowId, IsLegal {
    override fun isLegal(): Boolean = listOfNotNull(
        connectionFileJson,
        connectionFilePath,
        pythonExecutablePath
    ).size == 1
}
