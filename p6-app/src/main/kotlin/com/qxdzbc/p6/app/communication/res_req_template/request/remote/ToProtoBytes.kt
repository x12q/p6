package com.qxdzbc.p6.app.communication.res_req_template.request.remote

import com.google.protobuf.ByteString

interface ToProtoBytes {
    fun toProtoBytes():ByteString
}
