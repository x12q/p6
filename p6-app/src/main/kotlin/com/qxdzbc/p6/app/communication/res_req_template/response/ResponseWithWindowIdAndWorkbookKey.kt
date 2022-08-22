package com.qxdzbc.p6.app.communication.res_req_template.response

import com.qxdzbc.p6.app.communication.res_req_template.WithWindowId

interface ResponseWithWindowIdAndWorkbookKey : WithWindowId, ResponseWithWorkbookKeyTemplate

interface ResponseWithWindowIdAndWorkbookKey2 : ResponseWithWindowIdAndWorkbookKey, WithErrorIndicator


