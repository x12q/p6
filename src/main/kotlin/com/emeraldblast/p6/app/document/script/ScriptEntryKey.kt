package com.emeraldblast.p6.app.document.script

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.proto.ScriptProtos

data class ScriptEntryKey(
    val name:String,
    val wbKey: WorkbookKey?=null,
){
    companion object{
        fun ScriptProtos.ScriptEntryKeyProto.toModel(): ScriptEntryKey {
            return ScriptEntryKey(
                wbKey = if (this.hasWorkbookKey()) this.workbookKey.toModel() else null,
                name = this.name,
            )
        }

        fun fromProto(proto: ScriptProtos.ScriptEntryKeyProto): ScriptEntryKey {
            return proto.toModel()
        }
    }

    fun repStr():String{
        return "Script \"${name}\"" + if(wbKey!=null) " at ${wbKey.name}" else ""
    }

    fun setWbKey(workbookKey: WorkbookKey?):ScriptEntryKey{
        return this.copy(wbKey=workbookKey)
    }

    fun toProto(): ScriptProtos.ScriptEntryKeyProto {
        return ScriptProtos.ScriptEntryKeyProto.newBuilder()
            .apply {
                if (this@ScriptEntryKey.wbKey!=null){
                    this.workbookKey = this@ScriptEntryKey.wbKey.toProto()
                }
                this.name = this@ScriptEntryKey.name
            }
            .build()
    }

    fun isAppScript():Boolean{
        return this.wbKey == null
    }

    fun isWbScript():Boolean{
        return this.wbKey!=null
    }

    fun isSameContainer(another:ScriptEntryKey):Boolean{
        return this.wbKey == another.wbKey
    }
}
