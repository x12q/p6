package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import kotlin.reflect.KClass

class MultiCellUpdateResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w
