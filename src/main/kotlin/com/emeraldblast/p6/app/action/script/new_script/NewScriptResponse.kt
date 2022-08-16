package com.emeraldblast.p6.app.action.script.new_script

import com.emeraldblast.p6.app.communication.event.WithP6EventLookupClazz
import com.emeraldblast.p6.app.action.common_data_structure.SingleSignalResponse
import com.emeraldblast.p6.app.action.common_data_structure.SingleSignalResponseInterface
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.google.protobuf.ByteString
import kotlin.reflect.KClass

class NewScriptResponse(
    val r: SingleSignalResponseInterface
) : SingleSignalResponseInterface by r, WithP6EventLookupClazz {

    companion object {
        fun fromProtoBytes(data:ByteString): NewScriptResponse {
            return NewScriptResponse(SingleSignalResponse.fromProtoBytes(data))
        }
        fun fromErrReport(errReport:ErrorReport?): NewScriptResponse {
            return NewScriptResponse(SingleSignalResponse(errReport))
        }
    }

    override val p6EventLookupClazz: KClass<out Any>
        get() = NewScriptResponse::class
}
