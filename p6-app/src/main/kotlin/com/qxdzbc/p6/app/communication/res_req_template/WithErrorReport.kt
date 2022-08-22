package com.qxdzbc.p6.app.communication.res_req_template

import com.qxdzbc.p6.common.exception.error.ErrorReport

interface WithErrorReport {
    val errorReport:ErrorReport?
}
