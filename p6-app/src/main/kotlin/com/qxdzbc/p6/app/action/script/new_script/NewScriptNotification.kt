package com.qxdzbc.p6.app.action.script.new_script

import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.common_data_structure.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ScriptResponse
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntry.Companion.toModel
import com.qxdzbc.p6.proto.ScriptProtos.NewScriptNotificationProto
import com.google.protobuf.ByteString

class NewScriptNotification(
    val scriptEntries:List<ScriptEntry>,
    override val errorIndicator: ErrorIndicator
):ScriptResponse{
    companion object {
        fun fromProtoBytes(data:ByteString): NewScriptNotification {
            return NewScriptNotificationProto.newBuilder().mergeFrom(data).build().toModel()
        }
        fun NewScriptNotificationProto.toModel(): NewScriptNotification {
            return NewScriptNotification(
                scriptEntries = this.scriptEntriesList.map{
                    it.toModel()
                },
                errorIndicator = this.errorIndicator.toModel()
            )
        }
    }

    fun toProto():NewScriptNotificationProto{
        return NewScriptNotificationProto.newBuilder()
            .addAllScriptEntries(
                this@NewScriptNotification.scriptEntries.map{it.toProto()}
            )
            .setErrorIndicator(this.errorIndicator.toProto())
            .build()
    }

    override fun isLegal(): Boolean {
        val errorIndicatorIsLegal = errorIndicator.isLegal()
        if(errorIndicatorIsLegal){
            if(errorIndicator.isError){
                return this.scriptEntries.isEmpty()
            }else{
                return true
            }
        }else{
            return false
        }
    }
}
