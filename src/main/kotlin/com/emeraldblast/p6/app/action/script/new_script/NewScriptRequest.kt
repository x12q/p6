package com.emeraldblast.p6.app.action.script.new_script

import com.emeraldblast.p6.app.communication.res_req_template.request.remote.ScriptRequest
import com.emeraldblast.p6.app.document.script.ScriptEntry
import com.emeraldblast.p6.app.document.script.ScriptEntry.Companion.toModel
import com.emeraldblast.p6.proto.ScriptProtos.NewScriptRequestProto
import com.google.protobuf.ByteString

data class NewScriptRequest(val scriptEntry: ScriptEntry): ScriptRequest {

    companion object {
        fun fromProto(proto:NewScriptRequestProto): NewScriptRequest {
            return proto.toModel()
        }
        fun NewScriptRequestProto.toModel(): NewScriptRequest {
            return NewScriptRequest(
                scriptEntry = scriptEntry.toModel()
            )
        }
    }

    fun toProto(): NewScriptRequestProto {
        return NewScriptRequestProto.newBuilder()
            .setScriptEntry(this.scriptEntry.toProto())
            .build()
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
}
