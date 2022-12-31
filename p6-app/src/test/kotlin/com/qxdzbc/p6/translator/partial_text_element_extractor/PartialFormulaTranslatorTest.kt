package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.translator.P6Translator
import test.BaseAppStateTest

internal class PartialFormulaTranslatorTest : BaseAppStateTest() {
    lateinit var translator: P6Translator<String?>

//    @BeforeTest
//    override fun b() {
//        super.b()
//        translator = ts.p6Comp.partialTranslator()
//    }
//
//    @Test
//    fun translator() {
//        val m = mapOf(
//            "=A1" to "A1",
//            "=\$A1" to "\$A1",
//            "=\$A\$1" to "\$A\$1",
//            "=F1(A2" to "A2",
//            "=1+A3" to "A3",
//            "=1+A\$3" to "A\$3",
//            "=1+\$A\$3" to "\$A\$3",
//            "=1*((\$A\$3))" to "\$A\$3",
//            "=F2(1+2+A1:B33" to "A1:B33",
//            "=F2(1+2+\$A1:B33" to "\$A1:B33",
//            "=F2(\"A3\"" to null,
//            "=F2(" to null,
//            "=F2()" to null,
//            "=F2(\"qwe\"+A2" to "A2",
//            "=F2(A" to null,
//
//        )
//        for ((i, o) in m) {
//            assertEquals(Ok(o), translator.translate(i))
//        }
//    }
//
//    @Test
//    fun translator_fail() {
//        val fail = listOf(
//            "A3",
//            "1+A3",
//            "1+ \"A3\"",
//
//        )
//        for (f in fail) {
//            assertTrue(translator.translate(f) is Err)
//        }
//    }
}
