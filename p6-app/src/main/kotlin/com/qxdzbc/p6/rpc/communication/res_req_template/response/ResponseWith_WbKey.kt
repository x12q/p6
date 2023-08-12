package com.qxdzbc.p6.rpc.communication.res_req_template.response

import com.qxdzbc.p6.rpc.communication.res_req_template.IsError
import com.qxdzbc.p6.rpc.communication.res_req_template.WithErrorReport
import com.qxdzbc.p6.rpc.communication.res_req_template.WithWorkbookKey

interface ResponseWith_WbKey : IsError, WithWorkbookKey, WithErrorReport, Response
