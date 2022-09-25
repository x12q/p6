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
import com.qxdzbc.p6.translator.formula.function_def.AbstractFunctionDef
import com.qxdzbc.p6.translator.formula.function_def.FunctionDef
import org.mockito.kotlin.mock
import kotlin.reflect.KFunction
import kotlin.test.*


internal class FuncUnitTest {
    fun add(n1: Int, n2: Int): Result<Int, ErrorReport> {
        return Ok(n1 + n2)
    }

    fun toUpper(str: String): Result<String, ErrorReport> {
        return Ok(str.uppercase())
    }
    fun someFunction(i:Int): Result<Int, ErrorReport> {
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
            listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3")), RangeAddress("M1:Q3")),
            u.getRanges()
        )
    }
}

