package com.qxdzbc.p6.app.communication.res_req_template.request.remote

import com.qxdzbc.p6.app.communication.res_req_template.ToP6Msg
import com.qxdzbc.p6.app.communication.res_req_template.request.Request

/**
 * Remote requests are request sent to ipython
 */
interface RemoteRequest : Request,ToP6Msg{
}
