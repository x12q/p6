package com.qxdzbc.p6.app.action.cell.cell_multi_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.communication.event.WithP6EventLookupClazz
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString
import kotlin.reflect.KClass

class CellMultiUpdateResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w, WithP6EventLookupClazz {
    companion object {
        fun fromProtoBytes(data: ByteString,translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>): CellMultiUpdateResponse {
            val w = WorkbookUpdateCommonResponse.fromProtoBytes(data,translatorGetter)
            return CellMultiUpdateResponse(w)
        }
    }

    override val p6EventLookupClazz: KClass<out Any>
        get() = CellMultiUpdateResponse::class
}
