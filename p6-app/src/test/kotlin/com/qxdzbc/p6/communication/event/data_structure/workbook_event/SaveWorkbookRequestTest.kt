package com.qxdzbc.p6.communication.event.data_structure.workbook_event

import com.qxdzbc.p6.composite_actions.app.save_wb.SaveWorkbookRequest
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveWorkbookRequestTest {

    @Test
    fun toProto() {
        val i = SaveWorkbookRequest(
            wbKey = WorkbookKey(
                "Book1", Path.of("folder/book1.txt")
            ),
            path = "folder123/book123.txt"
        )
        val o = i.toProto()
        assertEquals(
            i.wbKey.toProto(), o.wbKey
        )
        assertEquals(
            i.path, o.path
        )
    }
}
