package com.qxdzbc.common.flyweight

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.test.*
import com.qxdzbc.common.test_util.TestSplitter

class FlyweightTableImpTest :TestSplitter(){

    lateinit var table: FlyweightTableImp<Float>

    @BeforeTest
    fun beforeTest() {
        table = FlyweightTableImp()
    }

    @Test
    fun addMarkedAttr() {
        val attr = Flyweights.wrap(123f).upCounter()
        val msAttr=attr
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

        test("add 123 to table"){
            preCondition {
                t.getMarkedAttr(v).shouldBeNull()
            }
            val (t2,newAttrMs) = t.addAndGetMarkedAttr(v)
            t = t2
            postCondition {
                newAttrMs.attr shouldBe v
                newAttrMs.refCount shouldBe 1
                t2.getMarkedAttr(v) shouldBe newAttrMs
            }
        }

        test("add 123 to table again"){
            preCondition {
                t.getMarkedAttr(v).shouldNotBeNull()
            }
            val (t2,newAttrMs) = t.addAndGetMarkedAttr(v)
            postCondition {
                newAttrMs.attr shouldBe v
                newAttrMs.refCount shouldBe 2
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
                val (t2,newAttrMs) = t.addAndGetMarkedAttr(v)
                t = t2
                t.getMarkedAttr(v)?.refCount shouldBe 1
            }
            val t3 = t.changeCountIfPossible(v,1)
            postCondition {
                t3.getMarkedAttr(v).shouldNotBeNull()
                t3.getMarkedAttr(v)?.attr shouldBe v
                t3.getMarkedAttr(v)?.refCount shouldBe 2
            }
        }

        test("decrease count to zero"){
            var t = table
            preCondition {
                val (t2,newAttrMs) = t.addAndGetMarkedAttr(v)
                t = t2
                t.getMarkedAttr(v)?.refCount shouldBe 1
            }
            val t3 = t.changeCountIfPossible(v,-100)
            postCondition {
                t3.getMarkedAttr(v).shouldBeNull()
            }
        }
    }
}
