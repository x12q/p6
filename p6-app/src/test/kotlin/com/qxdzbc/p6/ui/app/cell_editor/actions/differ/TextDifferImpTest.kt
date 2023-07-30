package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.test_util.TestSplitter
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class TextDifferImpTest : TestSplitter() {
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
        val t1 = TextFieldValue("abc")
        val t2 = TextFieldValue("abcd")
        test {
            val r=diff.extractTextAdditionWithRangeOfNewText(t1, t2)
            postCondition {
                assertNotNull(r)
                assertEquals("d",r.text)
                assertEquals(t2.selection,r.range)
            }
        }
    }

    @Test
    fun extractTextAddition(){
        test {
            val differ = TextDifferImp()
            val newText = TextFieldValue("abc(45",selection = TextRange(4))
            val r=differ.extractTextAdditionWithRangeOfNewText(
                TextFieldValue("abc12345"),
                newText
            )
            postCondition {
                assertNotNull(r)
                assertEquals("(",r.text)
                assertEquals(newText.selection,r.range)
            }
        }
    }
}
