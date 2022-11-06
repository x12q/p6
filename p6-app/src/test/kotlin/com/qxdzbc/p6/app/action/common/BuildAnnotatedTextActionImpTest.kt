package com.qxdzbc.p6.app.action.common

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.OtherElement
import kotlin.test.*

internal class BuildAnnotatedTextActionImpTest{
    val act = BuildAnnotatedTextActionImp()
    @Test
    fun tTt(){
        //ab_._123_._._.qqq
        val input= listOf(
            OtherElement("ab",0 .. 1),
            OtherElement("123",4 .. 6),
            OtherElement("qqq",10 .. 12),
        )
        val out=act.buildAnnotatedText(input, emptyList())
        val expect = "ab"+" ".repeat(2) + "123" + " ".repeat(3) + "qqq"
        assertEquals(expect,out.toString())
    }
}
