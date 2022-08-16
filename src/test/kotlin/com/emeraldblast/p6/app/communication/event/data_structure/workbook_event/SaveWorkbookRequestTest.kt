package com.emeraldblast.p6.app.communication.event.data_structure.workbook_event

import com.emeraldblast.p6.app.action.app.save_wb.SaveWorkbookRequest
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
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
            i.wbKey.toProto(), o.workbookKey
        )
        assertEquals(
            i.path, o.path
        )
    }
}
