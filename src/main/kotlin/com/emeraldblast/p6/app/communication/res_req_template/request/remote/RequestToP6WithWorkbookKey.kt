package com.emeraldblast.p6.app.communication.res_req_template.request.remote

import com.emeraldblast.p6.app.communication.res_req_template.ToP6Msg
import com.emeraldblast.p6.app.communication.res_req_template.WithWorkbookKey
import com.emeraldblast.p6.app.communication.res_req_template.request.Request

interface RequestToP6WithWorkbookKey : RemoteRequest,WithWorkbookKey
