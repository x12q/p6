package com.qxdzbc.p6.app.communication.event.data_structure.range.paste_range

import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest
import com.qxdzbc.p6.app.communication.event.P6Events
import com.qxdzbc.p6.app.action.common_data_structure.WbWsImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import kotlin.test.Test
import kotlin.test.assertEquals

class PasteRangeRequestTest {
    @Test
    fun toP6Msg() {
        val req = PasteRangeRequest(
            anchorCell = CellAddress("A1"),
            wbWs = WbWsImp(
                wbKey = WorkbookKey(""),
                wsName = "ASD"
            ),
            windowId = null
        )
        val p6Msg = req.toP6Msg()
        assertEquals(P6Events.Range.PasteRange.event, p6Msg.event)
    }
}
