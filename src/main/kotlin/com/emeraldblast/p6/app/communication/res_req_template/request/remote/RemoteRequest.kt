package com.emeraldblast.p6.app.communication.res_req_template.request.remote

import com.emeraldblast.p6.app.communication.res_req_template.ToP6Msg
import com.emeraldblast.p6.app.communication.res_req_template.request.Request

/**
 * Remote requests are request sent to ipython
 */
interface RemoteRequest : Request,ToP6Msg{
}
