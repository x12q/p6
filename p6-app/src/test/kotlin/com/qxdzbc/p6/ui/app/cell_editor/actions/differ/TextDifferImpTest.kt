package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class TextDifferImpTest{
    @Test
    fun `runTextDiff - addition`(){
        val differ = TextDifferImp()
        val old = TextFieldValue("abcd")
        val new = TextFieldValue("abcd---")

        val rs = differ.runTextDif(old,new)
        rs shouldBe TextDiffResult.Addition(
            addition = TextAndRange("---",range=TextRange(4,6))
        )
    }

    @Test
    fun `runTextDiff - deletion`(){
        val differ = TextDifferImp()
        val old = TextFieldValue("abcd---")
        val new = TextFieldValue("abcd")

        val rs = differ.runTextDif(old,new)
        rs shouldBe TextDiffResult.Deletion(
            TextRange(start=4,end=6)
        )
    }

    @Test
    fun `runTextDiff - deletion 2`(){
        val differ = TextDifferImp()
        val old = TextFieldValue("ab---d")
        val new = TextFieldValue("abd")

        val rs = differ.runTextDif(old,new)
        rs shouldBe TextDiffResult.Deletion(
            TextRange(start=2,end=4)
        )
    }


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
    fun extractTextAddition(){
        val differ = TextDifferImp()
        val t2 = TextFieldValue("abc(45",selection = TextRange(4))
        val r=differ.extractTextAddition(
            TextFieldValue("abc12345"),
            t2
        )
        assertNotNull(r)
        assertEquals("(",r.text)
        assertEquals(t2.selection,r.range)
    }
}
