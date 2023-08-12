package com.qxdzbc.p6.composite_actions.common_data_structure

import com.qxdzbc.p6.rpc.communication.res_req_template.response.Response
import com.qxdzbc.p6.rpc.communication.res_req_template.WithErrorReport
import com.qxdzbc.p6.proto.CommonProtos

interface SingleSignalResponseInterface : Response,
    WithErrorReport {
    fun toProto(): CommonProtos.SingleSignalResponseProto
}
