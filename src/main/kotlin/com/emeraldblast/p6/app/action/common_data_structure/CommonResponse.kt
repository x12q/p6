package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.communication.res_req_template.response.WithErrorIndicator

interface CommonResponse<T> :WithErrorIndicator {
    val data:T?
}
