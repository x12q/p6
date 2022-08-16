package com.emeraldblast.p6.app.communication.res_req_template

import com.emeraldblast.p6.common.exception.error.ErrorReport

interface WithErrorReport {
    val errorReport:ErrorReport?
}
