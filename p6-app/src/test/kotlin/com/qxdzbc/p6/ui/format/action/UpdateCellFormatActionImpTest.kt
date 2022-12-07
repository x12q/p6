package com.qxdzbc.p6.ui.format.action

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import test.BaseTest
import kotlin.test.*

internal class UpdateCellFormatActionImpTest : BaseTest(){

    lateinit var action:UpdateCellFormatActionImp
    lateinit var cellFormatTableMs:Ms<CellFormatTable>
    val ffTable get()=cellFormatTableMs.value.floatValueFormatTable

    @BeforeTest
    override fun b(){
        super.b()
        cellFormatTableMs = ms(CellFormatTableImp())
        action = UpdateCellFormatActionImp(
            cellFormatTableMs = cellFormatTableMs,
            stateContainerSt = comp.stateContMs()
        )
    }

    @Test
    fun setTextSize() {
        val wbwsSt = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!

        val v1 = 123f
        test("Set cell A1 text size to 123"){
            preCondition {
                ffTable.getMarkedAttr(v1).shouldBeNull()
            }
            action.setTextSize(
                cellId = CellId(CellAddress("A1"),wbwsSt),
                textSize=v1,
            )
            postCondition {
                val attrMs=ffTable.getMarkedAttr(v1)
                attrMs.shouldNotBeNull()
                attrMs.value.refCount shouldBe 1
            }
        }

        test("set cell B1 text size to 123"){
            action.setTextSize(
                cellId=CellId(CellAddress("B1"),wbwsSt),
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
                cellId = CellId(CellAddress("A1"),wbwsSt),
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
                cellId = CellId(CellAddress("B1"),wbwsSt),
                textSize=v3,
            )
            postCondition {
                ffTable.getMarkedAttr(v3)?.value?.refCount shouldBe 1
                ffTable.getMarkedAttr(v1).shouldBeNull()
            }
        }
    }
}
