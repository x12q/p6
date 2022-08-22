package com.qxdzbc.p6.app.communication.res_req_template.response

import com.qxdzbc.p6.app.communication.res_req_template.IsError
import com.qxdzbc.p6.app.communication.res_req_template.IsLegal
import com.qxdzbc.p6.app.communication.res_req_template.WithWindowId

@Deprecated("use ResponseWithWindowIdTemplate2 for new response classes. This is for older classes only")
interface ResponseWithWindowIdTemplate : WithWindowId, IsError, IsLegal,Response {
}
interface ResponseWithWindowIdTemplate2 : WithWindowId, WithErrorIndicator,Response,IsLegal
