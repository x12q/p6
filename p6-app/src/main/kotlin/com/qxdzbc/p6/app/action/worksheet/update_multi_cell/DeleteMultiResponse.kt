package com.qxdzbc.p6.app.action.worksheet.update_multi_cell

import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface

@Deprecated("dont use, this is for old api, to be deleted soon")
class DeleteMultiResponse(
    w: WorkbookUpdateCommonResponseInterface
) : WorkbookUpdateCommonResponseInterface by w {
}
