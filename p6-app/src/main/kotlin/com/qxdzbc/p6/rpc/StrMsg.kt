package com.qxdzbc.p6.rpc

import com.qxdzbc.p6.proto.CommonProtos.StrMsgProto

class StrMsg(val v:String) {
    fun toProto():StrMsgProto{
        return StrMsgProto.newBuilder().setV(v).build()
    }
}
