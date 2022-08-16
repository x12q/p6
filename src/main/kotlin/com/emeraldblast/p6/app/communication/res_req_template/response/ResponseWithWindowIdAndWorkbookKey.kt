package com.emeraldblast.p6.app.communication.res_req_template.response

import com.emeraldblast.p6.app.communication.res_req_template.WithWindowId

interface ResponseWithWindowIdAndWorkbookKey : WithWindowId, ResponseWithWorkbookKeyTemplate

interface ResponseWithWindowIdAndWorkbookKey2 : ResponseWithWindowIdAndWorkbookKey, WithErrorIndicator


