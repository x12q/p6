package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.function_def.AbstractFunctionDef
import com.qxdzbc.p6.translator.formula.function_def.FunctionDef
import org.mockito.kotlin.mock
import kotlin.reflect.KFunction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ExUnitTest {
    companion object {
        val c1 = CellAddress(1, 1)
        val c2 = CellAddress(3, 3)
    }

    class IntNum {
        val u = ExUnit.IntNum(100)

        @Test
        fun run() {
            assertEquals(Ok(100), u.run())
        }

        @Test
        fun shift() {
            assertEquals(u, u.shift(c1, c2))
        }

        @Test
        fun toFormula() {
            assertEquals(u._v.toString(), u.toFormula())
        }
    }

    class AddOperator {

        @Test
        fun getRange(){
            val u = ExUnit.AddOperator(
                ExUnit.CellAddressUnit(CellAddress("C3")),
                ExUnit.CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }

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

    class MinusOperator{
        @Test
        fun getRange(){
            val u = ExUnit.MinusOperator(
                ExUnit.CellAddressUnit(CellAddress("C3")),
                ExUnit.CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }
    }

    class MultiplyOperator{
        @Test
        fun getRange(){
            val u = ExUnit.MinusOperator(
                ExUnit.CellAddressUnit(CellAddress("C3")),
                ExUnit.CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }
    }

    class Div{
        @Test
        fun getRange(){
            val u = ExUnit.Div(
                ExUnit.CellAddressUnit(CellAddress("C3")),
                ExUnit.CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }
    }

    class PowerBy{
        @Test
        fun getRange(){
            val u = ExUnit.PowerBy(
                ExUnit.CellAddressUnit(CellAddress("C3")),
                ExUnit.CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }
    }

    class UnarySubtract{
        @Test
        fun getRange(){
            val u = ExUnit.UnarySubtract(
                ExUnit.CellAddressUnit(CellAddress("C3")),
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3"))),
                u.getRanges()
            )
        }
    }

    internal class Func {
        fun add(n1: Int, n2: Int): Result<Int, ErrorReport> {
            return Ok(n1 + n2)
        }

        fun toUpper(str: String): Result<String, ErrorReport> {
            return Ok(str.uppercase())
        }
        fun someFunction(i:Int):Result<Int,ErrorReport>{
            return i.toOk()
        }

        val fMap: St<FunctionMap> = FunctionMapImp(
            m = mapOf(
                "add" to object : AbstractFunctionDef() {
                    override val name: String
                        get() = "add"
                    override val function: KFunction<Any> = ::add
                },
                "toUpper" to object : AbstractFunctionDef() {
                    override val name: String
                        get() = "add"
                    override val function: KFunction<Any> = ::toUpper
                },
                "internalFunction" to mock<FunctionDef>(),
                "someFunction" to object: AbstractFunctionDef() {
                    override val name: String
                        get() = "someFunction"
                    override val function: KFunction<Any> = ::someFunction
                }
            )
        ).toMs()

        @Test
        fun run() {
            val u1 = ExUnit.Func(
                funcName = "toUpper",
                args = listOf(
                    ExUnit.StrUnit("abc")
                ),
                functionMapSt = fMap,
            )
            val out = u1.run()
            assertEquals(Ok("ABC"), out)
            assertEquals("toUpper(\"abc\")", u1.toFormula())

            val u2 = ExUnit.Func(
                funcName = "add",
                args = listOf(
                    ExUnit.IntNum(3),
                    ExUnit.IntNum(4),
                ),
                functionMapSt = fMap
            )
            assertEquals(Ok(7), u2.run())
            assertEquals("add(3, 4)", u2.toFormula())
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
                                functionMapSt = fMap
                            ),
                        ),
                        functionMapSt = fMap
                    ),
                    ExUnit.IntNum(5),
                ),
                functionMapSt = fMap
            )
            assertEquals(Ok(3 + 2 + 2 + 5), u2.run())
            assertEquals("add(add(3, add(2, 2)), 5)", u2.toFormula())
        }

        @Test
        fun run3() {
            val u2 = ExUnit.Func(
                funcName = "add",
                args = listOf(
                    ExUnit.Nothing,
                    ExUnit.IntNum(2),
                ),
                functionMapSt = fMap
            )
            val rs = u2.run()
            assertTrue { rs is Err }
        }

        @Test
        fun toFormula() {
            val u = ExUnit.Func(
                funcName = "someFunction",
                args = listOf(
                    ExUnit.IntNum(23),
                ),
                functionMapSt = fMap
            )
            assertEquals("someFunction(23)", u.toFormula())
        }

        @Test
        fun getRange(){
            val u = ExUnit.Func(
                funcName = "Function",
                args = listOf(
                    ExUnit.MinusOperator(
                        ExUnit.CellAddressUnit(CellAddress("C3")),
                        ExUnit.CellAddressUnit(CellAddress("K3")),
                    ),
                    ExUnit.RangeAddressUnit(RangeAddress("M1:Q3"))
                ),
                functionMapSt = mock()
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3")),RangeAddress("M1:Q3")),
                u.getRanges()
            )
        }
    }

    class RangeAddressUnit {
        val r = RangeAddress("A1:B2")
        val u = ExUnit.RangeAddressUnit(rangeAddress = r)

        @Test
        fun getRanges(){
            assertEquals(listOf(r),u.getRanges())
        }

        @Test
        fun shift() {
            val from = CellAddress("F2")
            val toCell = CellAddress("Q9")

            assertEquals(ExUnit.RangeAddressUnit(r.shift(from, toCell)), u.shift(from, toCell))
        }

        @Test
        fun toFormula() {
            assertEquals("A1:B2", u.toFormula())
        }
    }

    class PrimitiveUnit {
        @Test
        fun toFormula() {
            assertEquals("TRUE", ExUnit.TRUE.toFormula())
            assertEquals("FALSE", ExUnit.FALSE.toFormula())
            assertEquals("1", ExUnit.IntNum(1).toFormula())
            assertEquals(1.23.toString(), ExUnit.DoubleNum(1.23).toFormula())
            assertEquals("\"abc\"", ExUnit.StrUnit("abc").toFormula())
        }

        @Test
        fun shift() {
            val c1 = CellAddress("A1")
            val c2 = CellAddress("Q9")
            assertEquals(ExUnit.TRUE, ExUnit.TRUE.shift(c1, c2))
            assertEquals(ExUnit.FALSE, ExUnit.FALSE.shift(c1, c2))
            assertEquals(ExUnit.IntNum(1), ExUnit.IntNum(1).shift(c1, c2))
            assertEquals(ExUnit.DoubleNum(1.23), ExUnit.DoubleNum(1.23).shift(c1, c2))
            assertEquals(ExUnit.StrUnit("abc"), ExUnit.StrUnit("abc").shift(c1, c2))
        }
    }
}
