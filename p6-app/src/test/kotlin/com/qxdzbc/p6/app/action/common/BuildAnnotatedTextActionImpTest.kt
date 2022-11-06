package com.qxdzbc.p6.app.action.common

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.OtherElement
import kotlin.test.*

internal class BuildAnnotatedTextActionImpTest{
    val act = BuildAnnotatedTextActionImp()
    @Test
    fun tTt(){
        val input= listOf(
            OtherElement("ab",0 .. 1),
            OtherElement("123",4 .. 7),
            OtherElement("qqq",10 .. 13),
        )
        val out=act.buildAnnotatedText(input, emptyList())
        val expect = "ab"+" ".repeat(3) + "123" + " ".repeat(3) + "qqq"
        assertEquals(expect,out.toString())
    }
}
