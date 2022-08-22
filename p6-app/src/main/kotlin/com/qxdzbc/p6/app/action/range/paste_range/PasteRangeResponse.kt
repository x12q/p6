package com.qxdzbc.p6.app.action.range.paste_range

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.communication.event.WithP6EventLookupClazz
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString
import kotlin.reflect.KClass

class PasteRangeResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w, WithP6EventLookupClazz {
    companion object {
        fun fromProtoBytes(
            data: ByteString,
            translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>
        ): PasteRangeResponse {
            val w = WorkbookUpdateCommonResponse.fromProtoBytes(data, translatorGetter)
            return PasteRangeResponse(w)
        }
    }

    override val p6EventLookupClazz: KClass<out Any> = PasteRangeResponse::class
}
