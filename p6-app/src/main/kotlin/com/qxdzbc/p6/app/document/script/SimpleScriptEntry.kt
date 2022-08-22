package com.qxdzbc.p6.app.document.script

import com.qxdzbc.p6.proto.DocProtos.SimpleScriptEntryProto

data class SimpleScriptEntry(
    val name :String,
    val script:String,
){
    companion object{
        fun fromProto(proto:SimpleScriptEntryProto):SimpleScriptEntry{
            return SimpleScriptEntry(
                name = proto.name,
                script = proto.script
            )
        }
    }

    fun toProto():SimpleScriptEntryProto{
        return SimpleScriptEntryProto.newBuilder()
            .setName(this.name)
            .setScript(this.script)
            .build()
    }
}
