package com.emeraldblast.p6.app.communication.event.data_structure.range.range_to_clipboard

import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.emeraldblast.p6.app.communication.event.P6Events
import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RangeToClipboardRequestTest {

    @Test
    fun toProto() {
        val m = RangeToClipboardRequest(
            rangeId = RangeId(
                rangeAddress = RangeAddress("C1:J2"),
                wbKey = WorkbookKey("bb"),
                wsName = "QWE"
            ),
            windowId = null
        )
        val p = m.toProto()
        assertEquals(m.rangeId.toProto(), p.rangeId)
        assertFalse(p.hasWindowId())

        val m2 = RangeToClipboardRequest(
            rangeId = m.rangeId,
            windowId = "windowId"
        )

        val p2 = m2.toProto()
        assertEquals(m2.rangeId.toProto(), p2.rangeId)
        assertTrue(p2.hasWindowId())
        assertEquals(m2.windowId, p2.windowId)
    }

    @Test
    fun toP6Msg(){
        val m = RangeToClipboardRequest(
            rangeId = RangeId(
                rangeAddress = RangeAddress("C1:J2"),
                wbKey = WorkbookKey("bb"),
                wsName = "QWE"
            ),
            windowId = null
        )
        val p6Msg = m.toP6Msg()
        assertEquals(P6Events.Range.RangeToClipboard.event,p6Msg.event)
        assertEquals(m.toProto().toByteString(),p6Msg.data)
    }
}
