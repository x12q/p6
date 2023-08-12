package com.qxdzbc.p6.file

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.CellValue.Companion.toCellValue
import com.qxdzbc.p6.document_data_layer.cell.CellContentImp
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookImp
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.worksheet.WorksheetImp
import com.qxdzbc.p6.file.loader.P6FileLoaderImp
import com.qxdzbc.common.compose.StateUtils.toMs
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.cell.CellImp
import com.qxdzbc.p6.file.saver.P6SaverImp
import test.TestSample
import java.nio.file.Files

import java.nio.file.Paths
import kotlin.test.*

class P6Loader_SaverTest {
    lateinit var testSample:TestSample
    @BeforeTest
    fun b(){
        testSample = TestSample()
    }

    @Test
    fun save_load() {
        val saver = P6SaverImp()
        val loader = P6FileLoaderImp(testSample.appState.translatorContainer)
        val wbkSt =  ms(WorkbookKey("wb1"))
        val wsnSt = ms("S1")
        val cell = CellImp(
            CellId(address = CellAddress("C5"),wbkSt,wsnSt),
            content = CellContentImp(
                cellValueMs = ms(123.toCellValue())
            )
        )
        val cell2 =CellImp(
            CellId(address = CellAddress("F10"),wbkSt,wsnSt),
            content = CellContentImp(
                cellValueMs = ms(456.toCellValue())
            )
        )

        val qweqwe = WorksheetImp(
            nameMs = wsnSt,
            wbKeySt = wbkSt
        ).apply{
            addOrOverwrite(cell)
            addOrOverwrite(cell2)
        }
        val wb = WorkbookImp(
            keyMs = wbkSt,
        ).apply{
            addMultiSheetOrOverwrite(listOf(
                qweqwe
            ))
        }

        val path = Paths.get("w1.txt").toAbsolutePath()
        val csvPath = Paths.get("w1.csv").toAbsolutePath()

        val srs = saver.saveAsProtoBuf(wb, path)
        val srs2 = saver.saveFirstWsAsCsv(wb,csvPath)
        assertTrue { srs is Ok }
        assertTrue { srs2 is Ok }

        val lRs = loader.loadToWb(path)
        assertTrue { lRs is Ok }
        val loadedWb = lRs.component1()!!

        wb.key = (WorkbookKey("w1.txt",path))
//        assertTrue(expectedWb.isSimilar(loadedWb))
        assertEquals(wb.key,loadedWb.key)
        assertEquals(wb.worksheets[0].wsName,loadedWb.worksheets[0].wsName)
//        assertEquals(expectedWb,loadedWb)

        Files.delete(path)
    }
}
