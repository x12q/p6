package com.qxdzbc.p6.app.action.worksheet.paste_range

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeIdDM
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.format2.FormatConfig
import com.qxdzbc.p6.ui.format2.FormatEntrySet
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import test.BaseAppStateTest
import kotlin.random.Random
import kotlin.test.*

internal class PasteRangeActionImpTest : BaseAppStateTest(){

    lateinit var action:PasteRangeActionImp
    lateinit var targetWbWsSt:WbWsSt
    lateinit var targetFormat: FormatConfig
    lateinit var sourceFormat: FormatConfig
    lateinit var shiftedSourceFormat: FormatConfig
    lateinit var clipboadCellMap:Map<String, Cell>
    lateinit var shiftedClipboardData :RangeCopy
    lateinit var targetRangeId :RangeIdDM
    lateinit var sourceRangeId :RangeIdDM
    val f7 = "F7"
    val g9 ="G9"
    val a1 = "A1"
    @BeforeTest
    fun b(){
        action = ts.comp.pasteRangeActionImp()
        targetWbWsSt = ts.wb1Ws1St

        sourceRangeId=RangeIdDM(
            rangeAddress = RangeAddress("$a1:B3"),
            wbKey = ts.wbKey2,
            wsName = ts.wsn1
        )
        targetRangeId = RangeIdDM(
            rangeAddress = RangeAddress("$f7:$g9"),
            wbKey = targetWbWsSt.wbKey,
            wsName = targetWbWsSt.wsName
        )


        targetFormat = FormatConfig(
            textSizeConfig = FormatEntrySet.valid(
                RangeAddress(g9) to Random.nextFloat()
            )
        )

        sourceFormat = FormatConfig(
            textSizeConfig = FormatEntrySet.valid(
                RangeAddress(a1) to Random.nextFloat()
            )
        )
        shiftedSourceFormat = sourceFormat.shift(sourceRangeId.rangeAddress.topLeft,targetRangeId.rangeAddress.topLeft)

        clipboadCellMap = listOf(
            f7, g9
        ).associateWith {
            IndCellImp(CellAddress(it), content = CellContentImp.randomNumericContent())
        }

        shiftedClipboardData = RangeCopy(
            rangeId = sourceRangeId,
            cells = clipboadCellMap.values.toList()
        )

        ts.comp.updateCellFormatAction().applyFormatConfig(
            ts.wb1Ws1St,targetFormat,false
        )
        ts.comp.updateCellFormatAction().applyFormatConfig(
            ts.wb2Ws1St,sourceFormat,false
        )
    }

    @Test
    fun paste() {
        /*
        paste range A1:B3 at wb2, ws1 -> range F7:G9, wb1, wsn1.
        F7 format: from null -> has text size
        G9 format: from has text size -> null
         */
        preCondition {
            ts.sc.getCellFormatTable(targetWbWsSt)?.also {
                it.getValidFormatConfigForRange(targetRangeId.rangeAddress) shouldBe targetFormat
            }
            ts.sc.getCellMs(ts.wbKey1,ts.wsn1, CellAddress(f7)).shouldBeNull()
            ts.sc.getCellMs(ts.wbKey1,ts.wsn1, CellAddress(g9)).shouldBeNull()
        }

        action.paste(
            targetRangeId = targetRangeId,
            shiftedClipboardData = shiftedClipboardData,
            targetFormat = targetFormat,
            shiftedSourceFormat = shiftedSourceFormat,
        )

        postCondition {
          afterPasteAssertion()
        }
    }

    @Test
    fun makePasteCommand() {

        val command = action.makePasteCommand(targetRangeId, shiftedClipboardData)

        command.run()
        postCondition("check run effect") {
            afterPasteAssertion()
        }

        command.undo()
        postCondition("check undo effect") {
            ts.sc.getCellFormatTable(targetWbWsSt)?.also {
                it.getValidFormatConfigForRange(targetRangeId.rangeAddress) shouldBe targetFormat
            }
            ts.sc.getCellMs(ts.wbKey1,ts.wsn1, CellAddress(f7)).shouldBeNull()
            ts.sc.getCellMs(ts.wbKey1,ts.wsn1, CellAddress(g9)).shouldBeNull()
        }

        command.run()
        postCondition("check redo effect") {
            afterPasteAssertion()
        }
    }

    fun afterPasteAssertion(){
        ts.sc.getCellFormatTable(targetWbWsSt)?.also {
            it.getValidFormatConfigForRange(targetRangeId.rangeAddress) shouldBe shiftedSourceFormat
        }

        ts.sc.getCellMs(ts.wbKey1,ts.wsn1, CellAddress(f7))!!.value.content shouldBe clipboadCellMap[f7]!!.content

        ts.sc.getCellMs(ts.wbKey1,ts.wsn1, CellAddress(g9))!!.value.content shouldBe clipboadCellMap[g9]!!.content
    }
}
