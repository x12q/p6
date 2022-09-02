package com.qxdzbc.p6.app.communication.request_maker.range.range_to_clipboard

import com.qxdzbc.p6.app.action.request_maker.TemplateRM
import com.qxdzbc.p6.app.action.request_maker.TemplateRMSuspend
import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import org.mockito.kotlin.*
import kotlin.test.Test

class CopyRangeToClipboardRMImpTest {

    @Test
    fun makeReq() {
        val output = RangeToClipboardResponse(
            errorIndicator = ErrorIndicator(errorReport = null),
            rangeId = RangeIdImp(
                rangeAddress = RangeAddress("C1:J2"),
                wbKey = WorkbookKey("bb"),
                wsName = "QWE"
            ),
            windowId = "windowId"
        )
        val mockTemplateRM = mock<TemplateRM>() {
            whenever(
                it.makeRequest3(
                    any<RangeToClipboardRequest>(),
                    any<(P6Response) -> RangeToClipboardResponse?>()
                )
            ) doReturn output
        }
        val templateRM2: TemplateRMSuspend =mock()

//        val rm = CopyRangeToClipboardRMImp(
//            templateRM = mockTemplateRM,
//            templateRmSus = templateRM2
//        )
//        rm.copyRangeToClipboard(mock())
//        verify(mockTemplateRM, times(1)).makeRequest3(
//            any<RangeToClipboardRequest>(),
//            any<(P6Response) -> RangeToClipboardResponse?>()
//        )
//        assertEquals(output, rm.copyRangeToClipboard(mock()))
    }
}
