package com.qxdzbc.p6.rpc.common_data_structure

import com.qxdzbc.p6.proto.CommonProtos.BoolMsgProto

object BoolMsg {
    fun Boolean.toBoolMsgProto():BoolMsgProto{
        return BoolMsgProto.newBuilder().setV(this).build()
    }
}
