package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.communication.res_req_template.IsLegal
import com.emeraldblast.p6.app.communication.res_req_template.WithErrorReport
import com.emeraldblast.p6.app.communication.res_req_template.response.Response
import com.emeraldblast.p6.app.communication.res_req_template.response.WithErrorIndicator
import com.emeraldblast.p6.proto.CommonProtos

interface SingleSignalResponseInterface : Response, WithErrorReport, IsLegal {
    fun toProto(): CommonProtos.SingleSignalResponseProto
}
