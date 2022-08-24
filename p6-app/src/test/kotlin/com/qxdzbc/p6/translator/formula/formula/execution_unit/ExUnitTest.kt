package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.AbstractFunctionDef
import com.qxdzbc.p6.translator.formula.FunctionDef
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlin.reflect.KFunction
import kotlin.test.*

internal class ExUnitTest {

    class IntNum {
        @Test
        fun run() {
            val u = ExUnit.IntNum(100)
            assertEquals(Ok(100), u.run())
        }
    }

    class AddOperator {
        @Test
        fun `run on number`() {
            val u = ExUnit.AddOperator(
                ExUnit.IntNum(1), ExUnit.DoubleNum(2.0)
            )
            assertEquals(Ok(3.0), u.run())
        }

        @Test
        fun `run on string`() {
            val u = ExUnit.AddOperator(
                ExUnit.StrUnit("abc"), ExUnit.StrUnit("qwe")
            )
            assertEquals(Ok("abcqwe"), u.run())
        }

    }

    internal class Func {
        fun add(n1: Int, n2: Int): Result<Int, ErrorReport> {
            return Ok(n1 + n2)
        }

        fun toUpper(str: String):  Result<String, ErrorReport>{
            return Ok(str.uppercase())
        }

        val fMap = FunctionMapImp(
            m = mapOf(
                "add" to object :AbstractFunctionDef() {
                    override val name: String
                        get() = "add"
                    override val function: KFunction<Any> = ::add
                },
                "toUpper" to object :AbstractFunctionDef() {
                    override val name: String
                        get() = "add"
                    override val function: KFunction<Any> = ::toUpper
                }
            )
        )

        @Test
        fun run() {
            val u1 = ExUnit.Func(
                funcName = "toUpper",
                args = listOf(
                    ExUnit.StrUnit("abc")
                ),
                functionMap = fMap
            )
            val out = u1.run()
            assertEquals(Ok("ABC"), out)

            val u2 = ExUnit.Func(
                funcName = "add",
                args = listOf(
                    ExUnit.IntNum(3),
                    ExUnit.IntNum(4),
                ),
                functionMap = fMap
            )
            assertEquals(Ok(7), u2.run())
        }

        @Test
        fun run2() {
            // compute: add(3+2+2+5)
            val u2 = ExUnit.Func(
                funcName = "add",
                args = listOf(
                    ExUnit.Func(
                        funcName = "add",
                        args = listOf(
                            ExUnit.IntNum(3),
                            ExUnit.Func(
                                funcName = "add",
                                args = listOf(
                                    ExUnit.IntNum(2),
                                    ExUnit.IntNum(2),
                                ),
                                functionMap = fMap
                            ),
                        ),
                        functionMap = fMap
                    ),
                    ExUnit.IntNum(5),
                ),
                functionMap = fMap
            )
            assertEquals(Ok(3 + 2 + 2 + 5), u2.run())
        }

        @Test
        fun run3() {
            val u2 = ExUnit.Func(
                funcName = "add",
                args = listOf(
                    ExUnit.Nothing,
                    ExUnit.IntNum(2),
                ),
                functionMap = fMap
            )
            val rs = u2.run()
            assertTrue { rs is Err }
        }
    }
}
