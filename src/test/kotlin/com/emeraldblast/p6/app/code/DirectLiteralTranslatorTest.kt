package com.emeraldblast.p6.app.code

import com.github.michaelbull.result.Ok
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DirectLiteralTranslatorTest{
    @Test
    fun trans(){
        val inputs = mapOf(
            "a" to "r\"\"\" a \"\"\"[1:-1]",
            "\"b" to "r\"\"\" \"b \"\"\"[1:-1]",
            "\"b\"" to "r\"\"\" \"b\" \"\"\"[1:-1]",
            "\\b" to "r\"\"\" \\b \"\"\"[1:-1]",
            "b\"" to "r\"\"\" b\" \"\"\"[1:-1]",
        )

        for((i,e) in inputs){
            val o = DirectLiteralTranslator.translate(i)
            assertTrue(o is Ok)
            assertEquals(e,o.value)
            println(e)
        }
    }
}
