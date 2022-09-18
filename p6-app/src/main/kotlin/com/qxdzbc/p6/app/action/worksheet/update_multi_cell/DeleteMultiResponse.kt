package com.qxdzbc.p6.app.action.worksheet.update_multi_cell

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.communication.event.WithP6EventLookupClazz
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString
import kotlin.reflect.KClass

@Deprecated("dont use, this is for old api, to be deleted soon")
class DeleteMultiResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w, WithP6EventLookupClazz {

    override val p6EventLookupClazz: KClass<out Any>
        get() = DeleteMultiResponse::class

}
