package com.qxdzbc.p6.app.action.common_data_structure

import com.qxdzbc.p6.app.communication.res_req_template.response.Response
import com.qxdzbc.p6.proto.CommonProtos

interface SingleSignalResponseInterface : Response,
    com.qxdzbc.p6.app.communication.res_req_template.WithErrorReport {
    fun toProto(): CommonProtos.SingleSignalResponseProto
}
