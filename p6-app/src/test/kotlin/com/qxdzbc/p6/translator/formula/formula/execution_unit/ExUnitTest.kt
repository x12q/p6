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
import com.qxdzbc.p6.translator.formula.execution_unit.*
import com.qxdzbc.p6.translator.formula.execution_unit.BoolUnit.Companion.FALSE
import com.qxdzbc.p6.translator.formula.execution_unit.BoolUnit.Companion.TRUE
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

    class IntUnitTest {
        val u = IntUnit(100)

        @Test
        fun run() {
            assertEquals(Ok(100), u.runRs())
        }

        @Test
        fun shift() {
            assertEquals(u, u.shift(c1, c2))
        }

        @Test
        fun toFormula() {
            assertEquals(u.v.toString(), u.toFormula())
        }
    }


    class MultiplyOperatorTest{
        @Test
        fun getRange(){
            val u = MinusOperator(
                CellAddressUnit(CellAddress("C3")),
                CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }
    }

    class DivOperatorTest{
        @Test
        fun getRange(){
            val u = DivOperator(
                CellAddressUnit(CellAddress("C3")),
                CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }
    }

    class PowerByTest{
        @Test
        fun getRange(){
            val u = PowerBy(
                CellAddressUnit(CellAddress("C3")),
                CellAddressUnit(CellAddress("K3"))
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
                u.getRanges()
            )
        }
    }

    class UnarySubtractTest{
        @Test
        fun getRange(){
            val u = UnarySubtract(
                CellAddressUnit(CellAddress("C3")),
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3"))),
                u.getRanges()
            )
        }
    }

    internal class FuncUnitTest {
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
            val u1 = FuncUnit(
                funcName = "toUpper",
                args = listOf(
                    StrUnit("abc")
                ),
                functionMapSt = fMap,
            )
            val out = u1.runRs()
            assertEquals(Ok("ABC"), out)
            assertEquals("toUpper(\"abc\")", u1.toFormula())

            val u2 = FuncUnit(
                funcName = "add",
                args = listOf(
                    IntUnit(3),
                    IntUnit(4),
                ),
                functionMapSt = fMap
            )
            assertEquals(Ok(7), u2.runRs())
            assertEquals("add(3, 4)", u2.toFormula())
        }

        @Test
        fun run2() {
            // compute: add(3+2+2+5)
            val u2 = FuncUnit(
                funcName = "add",
                args = listOf(
                    FuncUnit(
                        funcName = "add",
                        args = listOf(
                            IntUnit(3),
                            FuncUnit(
                                funcName = "add",
                                args = listOf(
                                    IntUnit(2),
                                    IntUnit(2),
                                ),
                                functionMapSt = fMap
                            ),
                        ),
                        functionMapSt = fMap
                    ),
                    IntUnit(5),
                ),
                functionMapSt = fMap
            )
            assertEquals(Ok(3 + 2 + 2 + 5), u2.runRs())
            assertEquals("add(add(3, add(2, 2)), 5)", u2.toFormula())
        }

        @Test
        fun run3() {
            val u2 = FuncUnit(
                funcName = "add",
                args = listOf(
                    NothingUnit,
                    IntUnit(2),
                ),
                functionMapSt = fMap
            )
            val rs = u2.runRs()
            assertTrue { rs is Err }
        }

        @Test
        fun toFormula() {
            val u = FuncUnit(
                funcName = "someFunction",
                args = listOf(
                    IntUnit(23),
                ),
                functionMapSt = fMap
            )
            assertEquals("someFunction(23)", u.toFormula())
        }

        @Test
        fun getRange(){
            val u = FuncUnit(
                funcName = "Function",
                args = listOf(
                    MinusOperator(
                        CellAddressUnit(CellAddress("C3")),
                        CellAddressUnit(CellAddress("K3")),
                    ),
                    RangeAddressUnit(RangeAddress("M1:Q3"))
                ),
                functionMapSt = mock()
            )
            assertEquals(
                listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3")),RangeAddress("M1:Q3")),
                u.getRanges()
            )
        }
    }

    class RangeAddressUnitTest {
        val r = RangeAddress("A1:B2")
        val u = RangeAddressUnit(rangeAddress = r)

        @Test
        fun getRanges(){
            assertEquals(listOf(r),u.getRanges())
        }

        @Test
        fun shift() {
            val from = CellAddress("F2")
            val toCell = CellAddress("Q9")

            assertEquals(RangeAddressUnit(r.shift(from, toCell)), u.shift(from, toCell))
        }

        @Test
        fun toFormula() {
            assertEquals("A1:B2", u.toFormula())
        }
    }

    class PrimitiveUnitTest {
        @Test
        fun toFormula() {
            assertEquals("TRUE", TRUE.toFormula())
            assertEquals("FALSE", FALSE.toFormula())
            assertEquals("1", IntUnit(1).toFormula())
            assertEquals(1.23.toString(), DoubleUnit(1.23).toFormula())
            assertEquals("\"abc\"", StrUnit("abc").toFormula())
        }

        @Test
        fun shift() {
            val c1 = CellAddress("A1")
            val c2 = CellAddress("Q9")
            assertEquals(TRUE, TRUE.shift(c1, c2))
            assertEquals(FALSE, FALSE.shift(c1, c2))
            assertEquals(IntUnit(1), IntUnit(1).shift(c1, c2))
            assertEquals(DoubleUnit(1.23), DoubleUnit(1.23).shift(c1, c2))
            assertEquals(StrUnit("abc"), StrUnit("abc").shift(c1, c2))
        }
    }
}
