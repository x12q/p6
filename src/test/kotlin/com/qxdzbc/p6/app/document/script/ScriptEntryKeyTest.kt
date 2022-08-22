package com.qxdzbc.p6.app.document.script

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ScriptEntryKeyTest {

    @Test
    fun toProto() {
        val k1 = ScriptEntryKey(
            wbKey = null,
            name = "123"
        )
        val p1 = k1.toProto()
        assertEquals(k1.name,p1.name)
        assertFalse(p1.hasWorkbookKey())

        val k2 = ScriptEntryKey(
            wbKey = WorkbookKey("b1"),
            name="234"
        )
        val p2 = k2.toProto()
        assertEquals(k2.name, p2.name)
        assertEquals(k2.wbKey?.toProto(), p2.workbookKey)

    }
}
