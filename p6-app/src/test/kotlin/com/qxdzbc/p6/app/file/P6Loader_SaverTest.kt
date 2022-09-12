package com.qxdzbc.p6.app.file

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellImp
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.app.file.loader.P6FileLoaderImp
import com.qxdzbc.common.compose.StateUtils.toMs
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.file.saver.P6SaverImp
import org.mockito.kotlin.mock
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
        val loader = P6FileLoaderImp(testSample.sampleAppStateMs())
        val cell = CellImp(
            address = CellAddress("A1"),
            content = CellContentImp(
                cellValueMs = 123.toCellValue().toMs()
            )
        )
        val wb = WorkbookImp(
            keyMs = WorkbookKey("wb1").toMs(),
        ).addMultiSheetOrOverwrite(listOf(
            WorksheetImp(
                nameMs = "S1".toMs(),
                wbKeySt = mock()
            ).addOrOverwrite(cell)
        ))

        val path = Paths.get("w1.txt").toAbsolutePath()

        val srs = saver.save(wb, path)
        assertTrue { srs is Ok }
        val lRs = loader.load(path)
        assertTrue { lRs is Ok }
        val loadedWb = lRs.component1()!!

        val expectedWb = wb.setKey(WorkbookKey("w1.txt",path))
        assertEquals(expectedWb.key,loadedWb.key)
        assertEquals(expectedWb.worksheets[0],(loadedWb.worksheets[0]))
        assertEquals(expectedWb,loadedWb)

        Files.delete(path)
    }
}
