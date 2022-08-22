package com.qxdzbc.p6.app.utils

import com.qxdzbc.p6.app.common.utils.TextUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class TextUtilsTest {

    @Test
    fun removeFirstAndLastChar() {
        assertEquals("abc", TextUtils.removeFirstAndLastChar("_abc'"))
        assertEquals("", TextUtils.removeFirstAndLastChar(""))
        assertEquals("", TextUtils.removeFirstAndLastChar("a"))
        assertEquals("", TextUtils.removeFirstAndLastChar("ab"))
    }

    @Test
    fun cleanExecutionResultText(){
        assertEquals("6", TextUtils.cleanExecutionResultText("6"))
        assertEquals("6", TextUtils.cleanExecutionResultText("'6'"))
        assertEquals("abc", TextUtils.cleanExecutionResultText("'abc'"))
        assertEquals("", TextUtils.cleanExecutionResultText("''"))
        assertEquals("\\\"abc\\\"", TextUtils.cleanExecutionResultText(""" '\\"abc\\"' """.trim()))
        val jsonInput = """
            '{"value": 123.0, "script": "cell(\\"@A1\\").value", "addr": [2, 1]}'
        """.trimIndent()
        val expectedOuput = """
            {"value": 123.0, "script": "cell(\"@A1\").value", "addr": [2, 1]}
        """.trimIndent()
        assertEquals(expectedOuput, TextUtils.cleanExecutionResultText(jsonInput))
    }
}
