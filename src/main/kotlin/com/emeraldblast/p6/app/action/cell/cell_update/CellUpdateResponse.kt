package com.emeraldblast.p6.app.action.cell.cell_update

import com.emeraldblast.p6.app.communication.event.WithP6EventLookupClazz
import com.emeraldblast.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.emeraldblast.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString
import kotlin.reflect.KClass

class CellUpdateResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w, WithP6EventLookupClazz {
    companion object {
        fun fromProtoBytes(data: ByteString,translatorGetter:(wbKey: WorkbookKey, wsName:String)->P6Translator<ExUnit>): CellUpdateResponse {
            val w = WorkbookUpdateCommonResponse.fromProtoBytes(data,translatorGetter)
            return CellUpdateResponse(w)
        }
    }

    override val p6EventLookupClazz: KClass<out Any>
        get() = CellUpdateResponse::class
}
