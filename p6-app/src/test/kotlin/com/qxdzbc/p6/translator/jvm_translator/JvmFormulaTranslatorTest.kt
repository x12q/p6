package com.qxdzbc.p6.translator.jvm_translator

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.AbstractFunctionDef
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.P6FunctionDefinitions
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit.Companion.exUnit
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.qxdzbc.common.compose.StateUtils.toSt
import com.github.michaelbull.result.Ok
import kotlin.test.*
import kotlin.math.pow
import com.github.michaelbull.result.Result
import java.nio.file.Path
import kotlin.reflect.KFunction

class JvmFormulaTranslatorTest {
    lateinit var functionMap: FunctionMap
    lateinit var translator: JvmFormulaTranslator
    val wbKey: WorkbookKey = WorkbookKey("b1")
    val wsName = "s1"

    @BeforeTest
    fun b() {
        fun add(n1: Int, n2: Int): Result<Int, ErrorReport> {
            return Ok(n1 + n2)
        }

        fun toUpper(str: String): Result<String, ErrorReport> {
            return Ok(str.uppercase())
        }
        functionMap = FunctionMapImp(
            mapOf(
                "add" to object : AbstractFunctionDef() {
                    override val name: String
                        get() = "add"
                    override val function: KFunction<Any> = ::add
                },
                "toUpper" to object : AbstractFunctionDef() {
                    override val name: String
                        get() = "add"
                    override val function: KFunction<Any> = ::toUpper
                }
            )
        )
        translator = JvmFormulaTranslator(
            treeExtractor = TreeExtractorImp(),
            visitor = JvmFormulaVisitor2(
                wbKeySt = wbKey.toSt(),
                wsNameSt = wsName.toSt(),
                functionMap = functionMap
            )
        )
    }

    @Test
    fun `translate cell access`() {
        val inputMap = mapOf(
            "=A1" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getCellRs,
                args = listOf(
                    wbKey.exUnit(), wsName.exUnit(), CellAddress("A1").exUnit(),
                ),
                functionMap = functionMap
            ),
        )
        for ((i, expect) in inputMap) {
            val rs = translator.translate(i)
            assertTrue { rs is Ok }
            val o = rs.component1()
            assertEquals(o, expect)
        }
    }

    @Test
    fun `translate function call`() {
        val inputMap = mapOf(
            "=F1(12,\"ax\",F2(A1),1+2.2)" to ExUnit.Func(
                funcName = "F1",
                args = listOf(
                    ExUnit.IntNum(12),
                    ExUnit.StrUnit("ax"),
                    ExUnit.Func(
                        funcName = "F2",
                        args = listOf(
                            ExUnit.Func(
                                funcName = P6FunctionDefinitions.getCellRs,
                                args = listOf(
                                    wbKey.exUnit(), wsName.exUnit(), CellAddress("A1").exUnit()
                                ),
                                functionMap = functionMap
                            )
                        ),
                        functionMap = functionMap
                    ),
                    ExUnit.AddOperator(
                        ExUnit.IntNum(1),
                        ExUnit.DoubleNum(2.2)
                    )
                ),
                functionMap = functionMap
            ),
        )
        for ((i, expect) in inputMap) {
            val rs = translator.translate(i)
            assertTrue { rs is Ok }
            val o = rs.component1()
            assertEquals(o, expect)
        }
    }

    @Test
    fun `translate get range from sheet`() {
        val inputMap = mapOf(

            "=\$A\$1" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getCellRs,
                args = listOf(
                    wbKey.exUnit(),
                    wsName.exUnit(),
                    CellAddress("\$A\$1").exUnit()
                ),
                functionMap = functionMap
            ),
            "=\$A1:B$3" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(
                    wbKey.exUnit(),
                    wsName.exUnit(),
                    RangeAddress("\$A1:B\$3").exUnit()
                ),
                functionMap = functionMap
            ),
            "=A1:B3" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(
                    wbKey.exUnit(),
                    wsName.exUnit(),
                    RangeAddress("A1:B3").exUnit()
                ),
                functionMap = functionMap
            ),
            "=A1:B3@Sheet1" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(
                    wbKey.exUnit(),
                    "Sheet1".exUnit(),
                    RangeAddress("A1:B3").exUnit()
                ),
                functionMap = functionMap
            ),
            "=A1:B3@'Sheet 13'" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(
                    wbKey.exUnit(),
                    "Sheet 13".exUnit(),
                    RangeAddress("A1:B3").exUnit()
                ),
                functionMap = functionMap
            ),
            "=A1:B3@'Sheet 13'@Wb1" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(
                    WorkbookKey("Wb1", null).exUnit(),
                    "Sheet 13".exUnit(),
                    RangeAddress("A1:B3").exUnit()
                ),
                functionMap = functionMap
            ),
            "=A1:B3@'Sheet 13'@Wb1@'path/to/wb.txt'" to ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(
                    WorkbookKey("Wb1", Path.of("path/to/wb.txt")).exUnit(),
                    "Sheet 13".exUnit(),
                    RangeAddress("A1:B3").exUnit()
                ),
                functionMap = functionMap
            )
        )
        for ((i, expect) in inputMap) {
            val o = translator.translate(i)
            assertTrue { o is Ok }
            val e = o.component1()
            assertEquals(expect, e)
        }
    }

    @Test
    fun `translate number literal`() {
        val inputMap = mapOf(
            "=123" to 123,
            "=((123))" to 123,
            "=123.32" to 123.32,
            "=(123.32)" to 123.32,
            "=-123" to -123.0,
            "=-(123)" to -123.0,
            "=(-123)" to -123.0,
            "=(-123.32)" to -123.32,
            "=-(123.32)" to -123.32,
            "=-(123.32)" to -123.32,
            "=-(123.32)" to -123.32,
            "=-(-123.32)" to 123.32,
            "=123123123123" to 123_123_123_123.0,
        )
        testTranslate(inputMap)
    }

    @Test
    fun `translate string literal`() {
        val inputMap = mapOf(
            "=\"abc\"" to "abc",
            "=\"\"" to "",
            "=(\"qwe\")" to "qwe"
        )
        testTranslate(inputMap)
    }

    @Test
    fun `translate bool literal`() {
        val inputMap = mapOf(
            "=TRUE" to true,
            "=FALSE" to false,
            "=(TRUE)" to true,
            "=((TRUE))" to true,
        )
        testTranslate(inputMap)
    }

    @Test
    fun `translate power by`() {
        testTranslate(
            mapOf(
                "=2^3" to 8.0,
                "=(2^3)" to 8.0,
                "=(2.5^3)" to 2.5.pow(3),
            )
        )
    }

    @Test
    fun `translate add, sub`() {
        testTranslate(
            mapOf(
                "=2+3" to 2 + 3.0,
                "=2+3+4" to 2 + 3.0 + 4,
                "=(2-3)" to 2 - 3.0,
                "=(2.5+3)-1" to 2.5 + 3 - 1,
                "=\"a\" + 1" to "a1",
                "=\"a\"+\"b\"+\"c\"" to "abc",
                "=\"a\"+\"\"" to "a",
            )
        )
    }

    @Test
    fun `translate multiply, division`() {
        testTranslate(
            mapOf(
                "=2/3" to 2 / 3.0,
                "=(2*3)" to 2 * 3.0,
                "=(2.5*3)" to 2.5 * 3,
                "=(2.5*3)-1" to 2.5 * 3 - 1,
                "=(1-3*6+2)/3" to (1 - 3 * 6.0 + 2) / 3,
                "=(1+1)*3" to (1 + 1) * 3.0,
                "=(2*3)/3" to (2 * 3) / 3.0
            )
        )
    }

    fun testTranslate(input: Map<String, Any>) {
        for ((i, o) in input) {
            val output = translator.translate(i)
            assertTrue (output is Ok,i)
            assertEquals(Ok(o), output.component1()!!.run())
        }
    }

}
