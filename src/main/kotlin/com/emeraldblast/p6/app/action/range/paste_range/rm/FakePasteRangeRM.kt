package com.emeraldblast.p6.app.action.range.paste_range.rm

import com.emeraldblast.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeRequest
import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeRequest2
import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeResponse
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import javax.inject.Inject

class FakePasteRangeRM @Inject constructor(): PasteRangeRM {
    override fun pasteRange(request: PasteRangeRequest): PasteRangeResponse? {
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

    override fun pasteRange2(request: PasteRangeRequest2): PasteRangeResponse? {
        TODO("Not yet implemented")
    }
}
