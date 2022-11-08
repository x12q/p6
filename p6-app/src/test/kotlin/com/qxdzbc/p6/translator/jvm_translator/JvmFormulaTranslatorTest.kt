package com.qxdzbc.p6.translator.jvm_translator

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.function_def.AbstractFunctionDef
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.github.michaelbull.result.Ok
import kotlin.test.*
import kotlin.math.pow
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.translator.formula.execution_unit.*
import com.qxdzbc.p6.translator.formula.execution_unit.CellAddressUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.RangeAddressUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.WbKeyStUnit.Companion.toExUnit
import test.TestSample
import kotlin.reflect.KFunction

class JvmFormulaTranslatorTest {
    lateinit var functionMap: Ms<FunctionMap>
    lateinit var translator: JvmFormulaTranslator
    lateinit var ts:TestSample

    val wbKeySt:St<WorkbookKey> get()=ts.wbKey1Ms
    val wbKey: WorkbookKey get()= wbKeySt.value
    lateinit var wsNameSt:St<String>
    val wsName get()= wsNameSt.value


    @BeforeTest
    fun b() {
        ts = TestSample()
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
        ).toMs()
        wsNameSt = ts.appState.docCont.getWsNameSt(wbKeySt,ts.wsn1)!!
        translator = JvmFormulaTranslator(
            treeExtractor = TreeExtractorImp(),
            visitor = JvmFormulaVisitor(
                wbKeySt = wbKeySt,
                wsNameSt = wsNameSt,
                functionMapMs = functionMap,
                docContMs = ts.appState.docContMs,
            )
        )
    }

    @Test
    fun `translate cell access`() {
        val inputMap = mapOf(
            "=A1" to GetCellUnit(
                funcName = P6FunctionDefinitions.getCellRs,
                cellAddressUnit= CellAddress("A1").toExUnit(),
                wsNameUnit=WsNameStUnit(wsNameSt),
                wbKeyUnit=wbKeySt.toExUnit(),
                functionMapSt = functionMap
            ),
        )
        for ((i, expect) in inputMap) {
            val rs = translator.translate(i)
            assertTrue { rs is Ok }
            val o = rs.component1()
            assertEquals(expect,o)
        }
    }

    @Test
    fun `translate function call`() {
        val inputMap = mapOf(
            "=F1(12,\"ax\",F2(A1),1+2.2)" to FuncUnit(
                funcName = "F1",
                args = listOf(
                    IntUnit(12),
                    StrUnit("ax"),
                    FuncUnit(
                        funcName = "F2",
                        args = listOf(
                            GetCellUnit(
                                funcName = P6FunctionDefinitions.getCellRs,
                                cellAddressUnit= CellAddress("A1").toExUnit(),
                                wsNameUnit=WsNameStUnit(wsNameSt),
                                wbKeyUnit=wbKeySt.toExUnit(),
                                functionMapSt = functionMap
                            )
                        ),
                        functionMapSt = functionMap
                    ),
                    AddOperator(
                        IntUnit(1),
                        DoubleUnit(2.2)
                    )
                ),
                functionMapSt = functionMap
            ),
        )
        for ((i, expect) in inputMap) {
            val rs = translator.translate(i)
            assertTrue { rs is Ok }
            val o = rs.component1()
            assertEquals(o, expect,i)
        }
    }

    @Test
    fun `translate get range, get cell`() {
        val validMap = mapOf(
            "=\$A\$1" to GetCellUnit(
                funcName = P6FunctionDefinitions.getCellRs,
                cellAddressUnit= CellAddress("\$A\$1").toExUnit(),
                wsNameUnit=WsNameStUnit(wsNameSt),
                wbKeyUnit=wbKeySt.toExUnit(),
                functionMapSt = functionMap
            ),
            "=\$A1:B$3" to GetRange(
                funcName = P6FunctionDefinitions.getRangeRs,
                    wbKeySt.toExUnit(),
                    WsNameStUnit(wsNameSt),
                    RangeAddress("\$A1:B\$3").toExUnit()
                ,
                functionMapSt = functionMap
            ),
            "=A1:B3" to GetRange(
                funcName = P6FunctionDefinitions.getRangeRs,
                    wbKeySt.toExUnit(),
                    WsNameStUnit(wsNameSt),
                    RangeAddress("A1:B3").toExUnit()
            ,
                functionMapSt = functionMap
            ),
            "=A1:B3@Sheet1" to GetRange(
                funcName = P6FunctionDefinitions.getRangeRs,
                    wbKeySt.toExUnit(),
                    WsNameStUnit(wsNameSt),
                    RangeAddress("A1:B3").toExUnit(),
                functionMapSt = functionMap
            ),
        )
        for ((i, expect) in validMap) {
            val o = translator.translate(i)
            assertTrue(o is Ok,i)
            val e = o.component1()
            assertEquals(expect, e,i+"\n")
        }

//        data class InvalidOutput(
//            val funcName:String,
//            val args:List<Any>
//        )
//
//        val invalidMap = mapOf(
//            "=A1:B3@'Sheet 13'" to InvalidOutput(
//                funcName = P6FunctionDefinitions.getRangeRs,
//                args = listOf(
//                    wbKeySt.value,
//                    "Sheet 13",
//                    RangeAddress("A1:B3")
//                ),
//            ),
//            "=A1:B3@'Sheet 13'@Wb1" to InvalidOutput(
//                funcName = P6FunctionDefinitions.getRangeRs,
//                args = listOf(
//                    WorkbookKey("Wb1", null),
//                    "Sheet 13",
//                    RangeAddress("A1:B3")
//                ),
//            ),
//            "=A1:B3@'Sheet 13'@Wb1@'path/to/wb.txt'" to InvalidOutput(
//                funcName = P6FunctionDefinitions.getRangeRs,
//                args = listOf(
//                    WorkbookKey("Wb1", Path.of("path/to/wb.txt")),
//                    "Sheet 13",
//                    RangeAddress("A1:B3")
//                ),
//            )
//        )
//        for ((i, expect) in invalidMap) {
//
//            val o = translator.translate(i)
//            assertTrue { o is Ok }
//            val e = o.component1()
//            assertNotNull(e)
//            assertTrue(e is FuncUnit)
//
//            assertEquals(expect, InvalidOutput(
//                e.funcName,e.args.withIndex().map { (i,e)->
//                    when(i){
//                        0 -> (e.run()as Rse<St<WorkbookKey>>).component1()!!.value
//                        1 -> (e.run() as Rse<St<String>>).component1()!!.value
//                        else-> e.run()!!
//                    }
//                }
//            ),i)
//        }
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
            assertEquals(Ok(o), output.component1()!!.runRs())
        }
    }

}
