package com.qxdzbc.p6.rpc.communication.res_req_template.response

import com.qxdzbc.p6.composite_actions.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.rpc.communication.res_req_template.IsError
import com.qxdzbc.p6.rpc.communication.res_req_template.WithErrorReport
import com.qxdzbc.common.error.ErrorReport

interface WithErrorReport : IsError,
    WithErrorReport {
    val errorIndicator: ErrorIndicator
    override val isError: Boolean
        get() = errorIndicator.isError
    override val errorReport: ErrorReport?
        get() = errorIndicator.errorReport
}
