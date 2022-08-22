package com.qxdzbc.p6.app.action.common_data_structure

import com.qxdzbc.p6.app.communication.res_req_template.IsLegal
import com.qxdzbc.p6.app.communication.res_req_template.WithErrorReport
import com.qxdzbc.p6.app.communication.res_req_template.response.Response
import com.qxdzbc.p6.app.communication.res_req_template.response.WithErrorIndicator
import com.qxdzbc.p6.proto.CommonProtos

interface SingleSignalResponseInterface : Response, WithErrorReport, IsLegal {
    fun toProto(): CommonProtos.SingleSignalResponseProto
}
