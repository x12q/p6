package com.qxdzbc.p6.app.action.range.paste_range.rm

import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest2
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import javax.inject.Inject

class FakePasteRangeRM @Inject constructor() : PasteRangeRM {
    override fun pasteRange(request: PasteRangeRequest2): PasteRangeResponse? {
        return PasteRangeResponse(
            WorkbookUpdateCommonResponse(
                errorReport = null,
                wbKey = WorkbookKey("Book1"),
                newWorkbook = WorkbookImp(
                    keyMs = WorkbookKey("Book1").toMs(),
                ).apply {
                    this.createNewWs("SheetX")
                }
            )
        )
    }
}
