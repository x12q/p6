package com.qxdzbc.p6.app.communication.res_req_template.request

import com.qxdzbc.p6.app.communication.res_req_template.WithWindowId
import com.qxdzbc.p6.app.communication.res_req_template.WithWorkbookKey
import com.qxdzbc.p6.app.communication.res_req_template.request.Request

interface RequestWithWorkbookKeyAndWindowId : Request, WithWorkbookKey, WithWindowId

