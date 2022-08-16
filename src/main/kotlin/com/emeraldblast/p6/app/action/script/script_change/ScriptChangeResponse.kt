package com.emeraldblast.p6.app.action.script.script_change

import com.emeraldblast.p6.app.communication.event.WithP6EventLookupClazz
import com.emeraldblast.p6.app.action.common_data_structure.SingleSignalResponse
import com.emeraldblast.p6.app.action.common_data_structure.SingleSignalResponseInterface
import com.google.protobuf.ByteString
import kotlin.reflect.KClass

class ScriptChangeResponse(val r: SingleSignalResponseInterface): SingleSignalResponseInterface by r,
    WithP6EventLookupClazz {
    companion object {
        fun fromProtoBytes(data: ByteString): ScriptChangeResponse {
            return ScriptChangeResponse(SingleSignalResponse.fromProtoBytes(data))
        }
    }
    override val p6EventLookupClazz: KClass<out Any>
        get() = ScriptChangeResponse::class
}
