package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.input.TextFieldValue
import kotlin.test.*

internal class TextDifferImpTest{
    @Test
    fun ttt(){
        val diff = TextDifferImp()
        val t2 = TextFieldValue("abcd")
        val r=diff.extractTextAddition(
            TextFieldValue("abc"),
            t2
        )
        assertNotNull(r)
        assertEquals("d",r.text)
        assertEquals(t2.selection,r.range)
    }
}
