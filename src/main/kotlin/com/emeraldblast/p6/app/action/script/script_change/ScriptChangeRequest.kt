package com.emeraldblast.p6.app.action.script.script_change

import com.emeraldblast.p6.app.communication.res_req_template.request.remote.ScriptRequest
import com.emeraldblast.p6.app.document.script.ScriptEntryKey
import com.emeraldblast.p6.app.document.script.ScriptEntryKey.Companion.toModel
import com.emeraldblast.p6.proto.ScriptProtos.ScriptChangeRequestProto
import com.google.protobuf.ByteString


data class ScriptChangeRequest(
    val newScript:String,
    val key:ScriptEntryKey,
) : ScriptRequest {
    companion object{
        fun fromProtoBytes(data:ByteString): ScriptChangeRequest {
            val proto = ScriptChangeRequestProto.newBuilder()
                .mergeFrom(data).build()
            return ScriptChangeRequest(
                newScript = proto.newScript,
                key = proto.key.toModel()
            )
        }
    }

    fun toProto(): ScriptChangeRequestProto {
        return ScriptChangeRequestProto.newBuilder()
            .setNewScript(this.newScript)
            .setKey(this.key.toProto())
            .build()
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
}
