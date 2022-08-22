package com.qxdzbc.p6.app.code.script_builder

import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import org.junit.Assert.assertEquals
import org.junit.Test

class AllScriptBuilderImpTest {

    @Test
    fun createNewWorksheetSheet(){
        val b = AllScriptBuilderImp()
        b.createNewWorksheetRs("ABC")
        assertEquals(""".createNewWorksheetRs("ABC")""",b.build())
        val b2 = AllScriptBuilderImp()
        b2.createNewWorksheetRs()
        assertEquals(""".createNewWorksheetRs()""",b2.build())
    }

    @Test
    fun code1() {
        val b = AllScriptBuilderImp()
        b.getActiveWorkbook().getWorksheet("sheet1").cell("A1")
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1")""",b.build())
    }

    @Test
    fun code2() {
        val b = AllScriptBuilderImp()
        b.getWorkbook(1).getWorksheet("sheet1").cell("A1")
        assertEquals("""getWorkbook(1).getWorksheet("sheet1").cell("@A1")""",b.build())
    }

//    @Test
//    fun code3() {
//        val b = AllScriptBuilderImp()
//        val path = mock<Path>{
//            whenever { it.toString() }.thenReturn {  "path1" }
//            whenever { it.toAbsolutePath() }.thenReturn { it }
//        }
//
//        b.getWorkbook(WorkbookKey("Book1", path)).getWorksheet("sheet1").cell("A1")
//        assertEquals("""getWorkbook(WorkbookKeys.fromNameAndPath("Book1","path1")).getWorksheet("sheet1").cell("@A1")""",b.build())
//    }

    @Test
    fun code4() {
        val b = AllScriptBuilderImp()
        b.getActiveWorkbook().getWorksheet("sheet1").cell("A1").writeValue("123")
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1").value=123""",b.build())
    }

    @Test
    fun code5() {
        val b = AllScriptBuilderImp()
        b.getActiveWorkbook().getWorksheet("sheet1").cell("A1").writeValue("\"QWE\"")
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1").value="QWE"""",b.build())

        b.clear()
        b.getActiveWorkbook().getWorksheet("sheet1").cell("A1").readValue()
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1").value""",b.build())
    }
    @Test
    fun code6() {
        val b = AllScriptBuilderImp()
        b.getActiveWorkbook().getWorksheet("sheet1").cell("A1").writeFormula("\"QWE\"")
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1").formula="QWE"""",b.build())

        val b2 = AllScriptBuilderImp()
        b2.getActiveWorkbook().getWorksheet("sheet1").cell("A1").writeFormula("QWE")
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1").formula="QWE"""",b2.build())

        b2.clear()
        b2.getActiveSheet().cell("A1").readFormula()
        assertEquals("""getActiveWorksheet().cell("@A1").formula""",b2.build())
    }

    @Test
    fun code7() {
        val b = AllScriptBuilderImp()
        b.getActiveWorkbook().getWorksheet("sheet1").cell("A1").writeFormula("\"QWE\"")
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1").formula="QWE"""",b.build())

        val b2 = AllScriptBuilderImp()
        b2.getActiveWorkbook().getWorksheet("sheet1").cell("A1").writeFormula("QWE")
        assertEquals("""getActiveWorkbook().getWorksheet("sheet1").cell("@A1").formula="QWE"""",b.build())
    }

    @Test
    fun code8(){
        val b= AllScriptBuilderImp()
        b.cell(CellAddresses.fromIndices(1,200))
        assertEquals(".cell((1,200))",b.build())
    }

    @Test
    fun code9(){
        val b= AllScriptBuilderImp()
        b.cell("label")
        assertEquals(""".cell("@label")""",b.build())
    }

    @Test
    fun getWorksheet_index(){
        val b= AllScriptBuilderImp()
        b.getWorksheet(123)
        assertEquals(".getWorksheet(123)",b.build())
    }
    @Test
    fun getWorksheet_Name(){
        val b= AllScriptBuilderImp()
        b.getWorksheet("sheetName")
        assertEquals(""".getWorksheet("sheetName")""",b.build())
    }

    @Test
    fun getActiveWorkbook(){
        val b= AllScriptBuilderImp()
        b.getActiveWorkbook()
        assertEquals("""getActiveWorkbook()""",b.build())
    }

    @Test
    fun writeFormula(){
        val b= AllScriptBuilderImp()
        b.writeFormula("formula")
        assertEquals(""".formula="formula"""",b.build())
    }

    @Test
    fun readFormula(){
        val b= AllScriptBuilderImp()
        b.readFormula()
        assertEquals(""".formula""",b.build())
    }
}
