package com.qxdzbc.p6.app.action.cell.cell_multi_update

import com.qxdzbc.p6.app.communication.event.WithP6EventLookupClazz
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import kotlin.reflect.KClass

class MultiCellUpdateResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w, WithP6EventLookupClazz {

    override val p6EventLookupClazz: KClass<out Any>
        get() = MultiCellUpdateResponse::class
}
