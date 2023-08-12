package com.qxdzbc.p6.composite_actions.common

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.BasicTextElement
import kotlin.test.*

internal class BuildAnnotatedTextActionImpTest{
    val act = BuildAnnotatedTextActionImp()
    @Test
    fun tTt(){
        //ab_._123_._._.qqq
        val input= listOf(
            BasicTextElement("ab",0 .. 1),
            BasicTextElement("123",4 .. 6),
            BasicTextElement("qqq",10 .. 12),
        )
        val out=act.buildAnnotatedText(input, emptyList())
        val expect = "ab"+" ".repeat(2) + "123" + " ".repeat(3) + "qqq"
        assertEquals(expect,out.toString())
    }
}
