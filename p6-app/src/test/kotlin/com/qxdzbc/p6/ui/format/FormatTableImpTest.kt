package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import test.BaseTest
import kotlin.test.*

class FormatTableImpTest :BaseTest(){

    lateinit var table: FormatTableImp<Float>

    @BeforeTest
    fun beforeTest() {
        table = FormatTableImp()
    }

    @Test
    fun addMarkedAttr() {
        val attr = MarkedAttributes.valid(MockedAttr(123)).upCounter()
        val msAttr=ms(attr)
        table.getMarkedAttr(10f).shouldBeNull()
        val t2 = table.addMarkedAttr(10f,msAttr)
        t2.getMarkedAttr(10f) shouldBe msAttr
    }

    /**
     * add duplicate attribute
     */
    @Test
    fun add() {
        val v = 123f
        var t = table
        var attrMs:Ms<MarkedAttribute<Float>>? = null

        test("add 123 to table"){
            preCondition {
                t.getMarkedAttr(v).shouldBeNull()
            }
            val (t2,newAttrMs) = t.add(v)
            t = t2
            attrMs = newAttrMs
            postCondition {
                newAttrMs.value.attr.attrValue shouldBe v
                newAttrMs.value.refCount shouldBe 1
                t2.getMarkedAttr(v) shouldBe newAttrMs
            }
        }

        test("add 123 to table again"){
            preCondition {
                t.getMarkedAttr(v).shouldNotBeNull()
            }
            val (t2,newAttrMs) = t.add(v)
            postCondition {
                newAttrMs shouldBe attrMs
                newAttrMs.value.attr.attrValue shouldBe v
                newAttrMs.value.refCount shouldBe 2
                t2.getMarkedAttr(v) shouldBe newAttrMs
            }
        }
    }

    @Test
    fun changeCountBy(){
        val v=123f
        test("increase count of non-existing attr"){
            preCondition {
                table.getMarkedAttr(v).shouldBeNull()
            }
            val t2 = table.changeCountIfPossible(v,1)
            postCondition {
                t2.getMarkedAttr(v).shouldBeNull()
            }
        }

        test("increase count on existing attr"){
            var t = table
            preCondition {
                val (t2,newAttrMs) = t.add(v)
                t = t2
                t.getMarkedAttr(v)?.value?.refCount shouldBe 1
            }
            val t3 = t.changeCountIfPossible(v,1)
            postCondition {
                t3.getMarkedAttr(v).shouldNotBeNull()
                t3.getMarkedAttr(v)?.value?.attr?.attrValue shouldBe v
                t3.getMarkedAttr(v)?.value?.refCount shouldBe 2
            }
        }

        test("decrease count to zero"){
            var t = table
            preCondition {
                val (t2,newAttrMs) = t.add(v)
                t = t2
                t.getMarkedAttr(v)?.value?.refCount shouldBe 1
            }
            val t3 = t.changeCountIfPossible(v,-100)
            postCondition {
                t3.getMarkedAttr(v).shouldBeNull()
            }
        }
    }
}
