package com.emeraldblast.p6.app.action.app.restart_kernel

import com.emeraldblast.p6.app.action.common_data_structure.ErrorIndicator
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWindowIdTemplate2

data class RestartKernelResponse(
    override val errorIndicator: ErrorIndicator,
    override val windowId:String? = null,
) : ResponseWithWindowIdTemplate2 {
    override fun isLegal(): Boolean = errorIndicator.isLegal()
}
