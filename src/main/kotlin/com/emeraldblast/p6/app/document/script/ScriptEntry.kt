package com.emeraldblast.p6.app.document.script

import com.emeraldblast.p6.app.document.script.ScriptEntryKey.Companion.toModel
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.proto.ScriptProtos
import com.emeraldblast.p6.proto.ScriptProtos.ScriptEntryProto


data class ScriptEntry(
    val key:ScriptEntryKey,
    val script:String = "",
) {
    fun repStr():String{
        return "${key.repStr()}"
    }
    val wbKey get()=key.wbKey
    fun toSimpleEntry(): SimpleScriptEntry {
        return SimpleScriptEntry(
            name=this.name,
            script=script
        )
    }
    fun updateWbKey(newWbKey:WorkbookKey):ScriptEntry{
        return this.copy(
            key = this.key.copy(wbKey = newWbKey)
        )
    }
    val name:String get()=key.name

    companion object{
        fun fromProto(proto: ScriptProtos.ScriptEntryProto):ScriptEntry{
            return ScriptEntry(
                script = proto.script,
                key =  proto.key.toModel()
            )
        }
        fun ScriptEntryProto.toModel():ScriptEntry{
            return ScriptEntry.fromProto(this)
        }
    }
    fun toProto(): ScriptProtos.ScriptEntryProto {
        return ScriptProtos.ScriptEntryProto.newBuilder()
            .setScript(this.script)
            .setKey(this.key.toProto())
            .build()
    }
}
