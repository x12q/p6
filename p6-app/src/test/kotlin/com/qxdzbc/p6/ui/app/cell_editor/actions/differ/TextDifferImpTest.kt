package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.test.*

internal class TextDifferImpTest{
    @Test
    fun add(){
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

    @Test
    fun replace(){



        val diff = TextDifferImp()
        val t2 = TextFieldValue("abc(45",selection = TextRange(4))
        val r=diff.extractTextAddition(
            TextFieldValue("abc12345"),
            t2
        )
        assertNotNull(r)
        assertEquals("(",r.text)
        assertEquals(t2.selection,r.range)
    }
}
