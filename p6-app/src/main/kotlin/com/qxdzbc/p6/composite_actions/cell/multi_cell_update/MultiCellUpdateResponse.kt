package com.qxdzbc.p6.composite_actions.cell.multi_cell_update

import com.qxdzbc.p6.composite_actions.common_data_structure.WorkbookUpdateCommonResponseInterface

class MultiCellUpdateResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w
