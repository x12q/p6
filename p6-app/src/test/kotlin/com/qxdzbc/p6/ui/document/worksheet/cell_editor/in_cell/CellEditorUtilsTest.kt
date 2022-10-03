package com.qxdzbc.p6.ui.document.worksheet.cell_editor.in_cell

import com.qxdzbc.p6.ui.app.cell_editor.CellEditorUtils
import org.junit.Test

import org.junit.Assert.*

class CellEditorUtilsTest {

    @Test
    fun f() {
        val m = listOf(
            "=",
            "=S(",
            "=1+2+",
            "=1+2/",
            "=1+2*",
            "=1+2-",
            "=abc+",
            "=S(A1+",
            "=S(A1/",
            "=S(A1*",
            "=S(A1-",
            "=S(A1-(",
            "=S(A1:",
        )

        for (str in m) {
            assertTrue( CellEditorUtils.allowSelector(str))
        }

        val m2 = listOf(
            "",
            "A1+",
            "A1-",
            "A1*",
            "A1/",
            "1+2+",
            "1+2-",
            "1+2/",
            "1+2*",
            "A1:",
            "abc",
        )
        for (str in m2) {
            assertFalse( CellEditorUtils.allowSelector(str))
        }
    }
}
