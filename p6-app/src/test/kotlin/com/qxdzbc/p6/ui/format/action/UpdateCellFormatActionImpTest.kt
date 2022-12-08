package com.qxdzbc.p6.ui.format.action

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import test.BaseTest
import kotlin.properties.ReadOnlyProperty
import kotlin.test.*

internal class UpdateCellFormatActionImpTest : BaseTest(){

    lateinit var action:UpdateCellFormatActionImp
    lateinit var cellFormatTableMs:Ms<CellFormatTable>
    val ffTable get()=cellFormatTableMs.value.floatTable
    lateinit var wbwsSt: WbWsSt
    val cellA1 get()=CellId(CellAddress("A1"),wbwsSt)
    val cellB1 get()=CellId(CellAddress("B1"),wbwsSt)

    @BeforeTest
    override fun b(){
        super.b()
        cellFormatTableMs = ms(CellFormatTableImp())
        action = UpdateCellFormatActionImp(
            cellFormatTableMs = cellFormatTableMs,
            stateContainerSt = comp.stateContMs()
        )
        wbwsSt = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!
    }

    @Test
    fun testSetTextColor(){
        val c1 = Color(123)
        val c2 = Color(456)
        val cTable by ReadOnlyProperty{_,_->
            cellFormatTableMs.value.colorTable
        }
        test("set cell A1 text color to $c1"){
            preCondition {
                cTable.getMarkedAttr(c1).shouldBeNull()
                sc.getCellState(cellA1)?.textFormat?.textColor.shouldBeNull()
            }

            action.setTextColor(cellA1,c1)

            postCondition {
                sc.getCellState(cellA1)?.textFormat?.textColor shouldBe c1
                val mc=cTable.getMarkedAttr(c1)
                mc.shouldNotBeNull()
                mc.value.attr.attrValue shouldBe c1
                mc.value.refCount shouldBe 1
            }
        }

        test("set cell B1 text colo to $c1"){
            preCondition {
                sc.getCellState(cellB1)?.textFormat?.textColor.shouldBeNull()
            }

            action.setTextColor(cellB1,c1)

            postCondition {
                sc.getCellState(cellB1)?.textFormat?.textColor shouldBe c1
                val mc=cTable.getMarkedAttr(c1)
                mc.shouldNotBeNull()
                mc.value.attr.attrValue shouldBe c1
                mc.value.refCount shouldBe 2
            }
        }

        test("set cell A1 text colo to $c2"){

            action.setTextColor(cellA1,c2)

            postCondition {
                sc.getCellState(cellA1)?.textFormat?.textColor shouldBe c2
                val mc2=cTable.getMarkedAttr(c2)
                mc2.shouldNotBeNull()
                mc2.value.attr.attrValue shouldBe c2
                mc2.value.refCount shouldBe 1

                val mc1=cTable.getMarkedAttr(c1)
                mc1.shouldNotBeNull()
                mc1.value.attr.attrValue shouldBe c1
                mc1.value.refCount shouldBe 1
            }
        }

        test("set cell B1 text colo to $c2"){

            action.setTextColor(cellB1,c2)

            postCondition {
                sc.getCellState(cellB1)?.textFormat?.textColor shouldBe c2
                val mc2=cTable.getMarkedAttr(c2)
                mc2.shouldNotBeNull()
                mc2.value.attr.attrValue shouldBe c2
                mc2.value.refCount shouldBe 2

                val mc1=cTable.getMarkedAttr(c1)
                mc1.shouldBeNull()
            }
        }
    }

    @Test
    fun setTextSize() {
        val v1 = 123f
        test("Set cell A1 text size to 123"){
            preCondition {
                ffTable.getMarkedAttr(v1).shouldBeNull()
            }
            action.setTextSize(
                cellId = cellA1,
                textSize=v1,
            )
            postCondition {
                val attrMs=ffTable.getMarkedAttr(v1)
                attrMs.shouldNotBeNull()
                attrMs.value.refCount shouldBe 1
                sc.getCellStateMs(wbwsSt, cellA1.address)?.value?.textFormat.shouldNotBeNull()
            }
        }

        test("set cell B1 text size to 123"){
            action.setTextSize(
                cellId=cellB1,
                textSize=v1
            )
            postCondition {
                val attrMs=ffTable.getMarkedAttr(v1)
                attrMs.shouldNotBeNull()
                attrMs.value.refCount shouldBe 2
            }
        }

        val v2 = 456f
        test("Set cell A1 text size to 456"){
            preCondition {
                ffTable.getMarkedAttr(v2).shouldBeNull()
            }
            action.setTextSize(
                cellId = cellA1,
                textSize=v2,
            )
            postCondition {
                ffTable.getMarkedAttr(v2)?.value?.refCount shouldBe 1
                ffTable.getMarkedAttr(v1)?.value?.refCount shouldBe 1
            }
        }

        val v3 = 333f
        test("set B1 text size to 333"){
            preCondition {
                ffTable.getMarkedAttr(v3).shouldBeNull()
            }
            action.setTextSize(
                cellId = cellB1,
                textSize=v3,
            )
            postCondition {
                ffTable.getMarkedAttr(v3)?.value?.refCount shouldBe 1
                ffTable.getMarkedAttr(v1).shouldBeNull()
            }
        }
    }
}
