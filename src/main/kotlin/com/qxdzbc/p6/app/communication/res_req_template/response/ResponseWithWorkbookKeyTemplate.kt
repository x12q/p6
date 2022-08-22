package com.qxdzbc.p6.app.communication.res_req_template.response

import com.qxdzbc.p6.app.communication.res_req_template.IsError
import com.qxdzbc.p6.app.communication.res_req_template.IsLegal
import com.qxdzbc.p6.app.communication.res_req_template.WithErrorReport
import com.qxdzbc.p6.app.communication.res_req_template.WithWorkbookKey

interface ResponseWithWorkbookKeyTemplate : IsError, WithWorkbookKey, IsLegal, WithErrorReport, Response
