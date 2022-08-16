package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.app.common.proto.toModel
import com.emeraldblast.p6.app.common.proto.toProto
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.CommonProtos.SingleSignalResponseProto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.protobuf.ByteString

open class SingleSignalResponse(
    override val errorReport: ErrorReport? = null
) : SingleSignalResponseInterface {
    companion object {
        fun fromProtoBytes(data: ByteString): SingleSignalResponse {
            val proto = SingleSignalResponseProto.newBuilder().mergeFrom(data).build()
            return SingleSignalResponse(
                errorReport = proto.errorReport.toModel()
            )
        }
        fun fromRs(rs:Rs<Any,ErrorReport>): SingleSignalResponse {
            val out = when (rs) {
                is Ok -> {
                    SingleSignalResponse()
                }
                is Err -> {
                    SingleSignalResponse(rs.error)
                }
            }
            return out
        }
    }

    override fun toProto(): SingleSignalResponseProto {
        return SingleSignalResponseProto.newBuilder()
            .apply {
                val e = this@SingleSignalResponse.errorReport
                if(e!=null){
                    setErrorReport(e.toProto())
                }
            }
            .build()
    }

    override fun isLegal(): Boolean {
        return true
    }
}
