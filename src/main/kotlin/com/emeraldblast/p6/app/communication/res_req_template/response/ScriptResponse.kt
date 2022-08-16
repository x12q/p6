package com.emeraldblast.p6.app.communication.res_req_template.response

import com.emeraldblast.p6.app.communication.res_req_template.IsLegal

interface ScriptResponse : Response,WithErrorIndicator,IsLegal {
}
