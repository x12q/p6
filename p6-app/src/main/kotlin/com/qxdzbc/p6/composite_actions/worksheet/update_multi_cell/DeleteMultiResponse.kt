package com.qxdzbc.p6.composite_actions.worksheet.update_multi_cell

import com.qxdzbc.p6.composite_actions.common_data_structure.WorkbookUpdateCommonResponseInterface

@Deprecated("dont use, this is for old api, to be deleted soon")
class DeleteMultiResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w {
}
