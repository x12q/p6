package com.qxdzbc.p6.app.communication.res_req_template.response

import com.qxdzbc.p6.app.communication.res_req_template.IsLegal

interface ScriptResponse : Response,WithErrorIndicator,IsLegal {
}
