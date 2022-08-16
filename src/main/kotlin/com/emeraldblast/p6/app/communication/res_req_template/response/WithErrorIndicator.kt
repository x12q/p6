package com.emeraldblast.p6.app.communication.res_req_template.response

import com.emeraldblast.p6.app.action.common_data_structure.ErrorIndicator
import com.emeraldblast.p6.app.communication.res_req_template.IsError
import com.emeraldblast.p6.app.communication.res_req_template.WithErrorReport
import com.emeraldblast.p6.common.exception.error.ErrorReport

interface WithErrorIndicator : IsError, WithErrorReport {
    val errorIndicator: ErrorIndicator
    override val isError: Boolean
        get() = errorIndicator.isError
    override val errorReport: ErrorReport?
        get() = errorIndicator.errorReport
}
