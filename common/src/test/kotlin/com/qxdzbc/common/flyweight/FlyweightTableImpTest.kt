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
        val w = Flyweights.wrap(123f).increaseRefCount()
        table.getFlyweight(123f).shouldBeNull()
        val t2 = table.addFlyweight(w)
        t2.getFlyweight(123f) shouldBe w
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
                t.getFlyweight(v).shouldBeNull()
            }
            val (t2,newAttrMs) = t.addAndGetFlyweight(v)
            t = t2
            postCondition {
                newAttrMs.value shouldBe v
                newAttrMs.refCount shouldBe 1
                t2.getFlyweight(v) shouldBe newAttrMs
            }
        }

        test("add 123 to table again"){
            preCondition {
                t.getFlyweight(v).shouldNotBeNull()
            }
            val (t2,newAttrMs) = t.addAndGetFlyweight(v)
            postCondition {
                newAttrMs.value shouldBe v
                newAttrMs.refCount shouldBe 2
                t2.getFlyweight(v) shouldBe newAttrMs
            }
        }
    }

    @Test
    fun changeCountBy(){
        val v=123f
        test("increase count of non-existing attr"){
            preCondition {
                table.getFlyweight(v).shouldBeNull()
            }
            val t2 = table.changeCountIfPossible(v,1)
            postCondition {
                t2.getFlyweight(v).shouldBeNull()
            }
        }

        test("increase count on existing attr"){
            var t = table
            preCondition {
                val (t2,newAttrMs) = t.addAndGetFlyweight(v)
                t = t2
                t.getFlyweight(v)?.refCount shouldBe 1
            }
            val t3 = t.changeCountIfPossible(v,1)
            postCondition {
                t3.getFlyweight(v).shouldNotBeNull()
                t3.getFlyweight(v)?.value shouldBe v
                t3.getFlyweight(v)?.refCount shouldBe 2
            }
        }

        test("decrease count to zero"){
            var t = table
            preCondition {
                val (t2,newAttrMs) = t.addAndGetFlyweight(v)
                t = t2
                t.getFlyweight(v)?.refCount shouldBe 1
            }
            val t3 = t.changeCountIfPossible(v,-100)
            postCondition {
                t3.getFlyweight(v).shouldBeNull()
            }
        }
    }
}
