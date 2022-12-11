package com.qxdzbc.p6.ui.format.action

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormatImp
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import com.qxdzbc.p6.ui.format.FormatTableImp
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import com.qxdzbc.p6.ui.format.attr.BoolAttr.Companion.toBoolAttr
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import test.BaseTest
import kotlin.properties.ReadOnlyProperty
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class UpdateCellFormatActionImpTest : BaseTest() {

    lateinit var action: UpdateCellFormatActionImp
    lateinit var cellFormatTableMs: Ms<CellFormatTable>
    val ffTable get() = cellFormatTableMs.value.floatTable
    lateinit var wbwsSt: WbWsSt
    val cellA1Id get() = CellId(CellAddress("A1"), wbwsSt)
    val cellB1Id get() = CellId(CellAddress("B1"), wbwsSt)
    val cellStateA1 = CellStates.blank(CellAddress("A1"))
    val cellStateB1 = CellStates.blank(CellAddress("B1"))


    @BeforeTest
    override fun b() {
        super.b()
        cellFormatTableMs = ms(CellFormatTableImp())
        action = UpdateCellFormatActionImp(
            cellFormatTableMs = cellFormatTableMs,
            stateContainerSt = comp.stateContMs()
        )
        wbwsSt = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!
    }

    @Test
    fun produceNewStateForNewCrossed(){
        test("set cell A1 crossed: true -> false"){
            val cellFormatTable = CellFormatTableImp().updateBoolTable(
                FormatTableImp<BoolAttr>()
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.FALSE)
                    .addOrUpdate(BoolAttr.FALSE)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextCrossed(true),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewCrossed(
                inputState = inputState,
                crossed = false
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextCrossed(false),
                    inputState.cellFormatTable.updateBoolTable(
                        inputState.cellFormatTable.boolTable
                            .reduceCountIfPossible(BoolAttr.TRUE)
                            .increaseCountIfPossible(BoolAttr.FALSE)
                    )
                )
                newState shouldBe expectedState
            }

        }
    }

    @Test
    fun produceNewStateForNewHorizontalAlignment() {
        test("set cell A1 text horizontal alignment: Center -> End") {
            val cellFormatTable = CellFormatTableImp().updateHorizontalAlignmentTable(
                FormatTableImp<TextHorizontalAlignment>()
                    .addOrUpdate(TextHorizontalAlignment.Center)
                    .addOrUpdate(TextHorizontalAlignment.Center)
                    .addOrUpdate(TextHorizontalAlignment.Start)
                    .addOrUpdate(TextHorizontalAlignment.Start)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setHorizontalAlignment(TextHorizontalAlignment.Center),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewHorizontalAlignment(
                inputState = inputState,
                alignment = TextHorizontalAlignment.End
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setHorizontalAlignment(TextHorizontalAlignment.End),
                    inputState.cellFormatTable.updateHorizontalAlignmentTable(
                        inputState.cellFormatTable.horizontalAlignmentTable
                            .reduceCountIfPossible(TextHorizontalAlignment.Center)
                            .addOrUpdate(TextHorizontalAlignment.End)

                    )
                )
                newState shouldBe expectedState
            }
        }
    }

    @Test
    fun applyNewState() {
        val newState = UpdateCellFormatActionImp.TargetState(
            cellState = CellStates.blank(cellA1Id.address),
            cellFormatTable = CellFormatTableImp().updateFloatTable(
                FormatTableImp<Float>().addOrUpdate(123f)
            )
        )
        test("update app state new state") {
            preCondition {
                sc.getCellState(cellA1Id) shouldNotBe newState.cellState
                cellFormatTableMs.value shouldNotBe newState.cellFormatTable
            }
            action.applyNewState(cellA1Id, newState)
            postCondition {
                sc.getCellState(cellA1Id) shouldBe newState.cellState
                cellFormatTableMs.value shouldBe newState.cellFormatTable
            }
        }
    }

    @Test
    fun produceNewState() {
        val cs = cellStateA1
        test("old format ref count is reduced, and new format attr is recorded") {
            val cellFormatTable = CellFormatTableImp().updateFloatTable(
                FormatTableImp<Float>()
                    .addOrUpdate(1f)
                    .addOrUpdate(1f)
                    .addOrUpdate(2f)
                    .addOrUpdate(2f)
            )
            val newTextFormat = TextFormatImp().copy(textSize = 3f)
            val newState = action.produceNewState(
                cs,
                newFormat = 3f,
                getCurrentFormat = {
                    1f
                },
                getFormatTable = {
                    cellFormatTable.floatTable
                },
                produceNewTextFormat = { t, tf ->
                    newTextFormat
                },
                produceNewCellFormatTable = {
                    cellFormatTable.updateFloatTable(it)
                }
            )
            postCondition {
                val expectedCellFormatTable = CellFormatTableImp().updateFloatTable(
                    FormatTableImp<Float>()
                        .addOrUpdate(1f)
                        .addOrUpdate(2f)
                        .addOrUpdate(2f)
                        .addOrUpdate(3f)
                )
                newState.shouldNotBeNull()
                newState.cellState.textFormat shouldBe newTextFormat
                newState.cellFormatTable shouldBe expectedCellFormatTable
            }
        }

        test("old format ref count is reduced to 0 and removed, and new format attr is recorded") {
            val cellFormatTable = CellFormatTableImp().updateFloatTable(
                FormatTableImp<Float>()
                    .addOrUpdate(1f)
                    .addOrUpdate(2f)
                    .addOrUpdate(2f)
            )
            val newTextFormat = TextFormatImp().copy(textSize = 3f)
            val newState = action.produceNewState(
                cs,
                newFormat = 3f,
                getCurrentFormat = {
                    1f
                },
                getFormatTable = {
                    cellFormatTable.floatTable
                },
                produceNewTextFormat = { t, tf ->
                    newTextFormat
                },
                produceNewCellFormatTable = {
                    cellFormatTable.updateFloatTable(it)
                }
            )
            postCondition {
                val expectedCellFormatTable = CellFormatTableImp().updateFloatTable(
                    FormatTableImp<Float>()
                        .addOrUpdate(2f)
                        .addOrUpdate(2f)
                        .addOrUpdate(3f)
                )
                newState.shouldNotBeNull()
                newState.cellState.textFormat shouldBe newTextFormat
                newState.cellFormatTable shouldBe expectedCellFormatTable
            }
        }
    }


    @Test
    fun produceNewStateForNewTextColor() {
        val c1 = Color(123)
        val c2 = Color(456)
        var a1 = cellStateA1
        var b1 = cellStateB1
        var cft: CellFormatTable = CellFormatTableImp()
        val colorTable by ReadOnlyProperty { _, _ ->
            cft.colorTable
        }

        test("set cell A1 text color to $c1") {
            val outputState = action.produceNewStateForNewTextColor(
                inputState = UpdateCellFormatActionImp.TargetState(a1, cft),
                color = c1
            )
            postCondition {
                outputState.shouldNotBeNull()
                val newA1 = outputState.cellState
                a1 = newA1
                cft = outputState.cellFormatTable
                newA1.textFormat?.textColor shouldBe c1
                val mc = colorTable.getMarkedAttr(c1)
                mc.shouldNotBeNull()
                mc.attr.attrValue shouldBe c1
                mc.refCount shouldBe 1
            }
        }

        test("set cell B1 text colo to $c1") {
            preCondition {
                cellStateB1.textFormat?.textColor.shouldBeNull()
            }

            val outState = action.produceNewStateForNewTextColor(
                UpdateCellFormatActionImp.TargetState(b1, cft),
                c1
            )

            postCondition {
                outState.shouldNotBeNull()
                b1 = outState.cellState
                outState.cellState.textFormat?.textColor shouldBe c1
                cft = outState.cellFormatTable
                val mc = colorTable.getMarkedAttr(c1)
                mc.shouldNotBeNull()
                mc.attr.attrValue shouldBe c1
                mc.refCount shouldBe 2
            }
        }

        test("set cell A1 text colo to $c2") {
            val outState = action.produceNewStateForNewTextColor(
                UpdateCellFormatActionImp.TargetState(a1, cft),
                c2
            )
            postCondition {
                outState.shouldNotBeNull()
                outState.cellState.textFormat?.textColor shouldBe c2
                cft = outState.cellFormatTable
                a1 = outState.cellState
                val mc2 = outState.cellFormatTable.colorTable.getMarkedAttr(c2)
                mc2.shouldNotBeNull()
                mc2.attr.attrValue shouldBe c2
                mc2.refCount shouldBe 1

                val mc1 = outState.cellFormatTable.colorTable.getMarkedAttr(c1)
                mc1.shouldNotBeNull()
                mc1.attr.attrValue shouldBe c1
                mc1.refCount shouldBe 1
            }
        }

        test("set cell B1 text colo to $c2") {

            val outState = action.produceNewStateForNewTextColor(
                UpdateCellFormatActionImp.TargetState(b1, cft),
                c2
            )

            postCondition {
                outState.shouldNotBeNull()
                cft = outState.cellFormatTable
                outState.cellState.textFormat?.textColor shouldBe c2
                val mc2 = colorTable.getMarkedAttr(c2)
                mc2.shouldNotBeNull()
                mc2.attr.attrValue shouldBe c2
                mc2.refCount shouldBe 2

                val mc1 = colorTable.getMarkedAttr(c1)
                mc1.shouldBeNull()
            }
        }
    }


    @Test
    fun testSetTextColor() {
        val c1 = Color(123)
        val c2 = Color(456)
        val cTable by ReadOnlyProperty { _, _ ->
            cellFormatTableMs.value.colorTable
        }
        test("set cell A1 text color to $c1") {
            preCondition {
                cTable.getMarkedAttr(c1).shouldBeNull()
                sc.getCellState(cellA1Id)?.textFormat?.textColor.shouldBeNull()
            }

            action.setTextColor(cellA1Id, c1)

            postCondition {
                sc.getCellState(cellA1Id)?.textFormat?.textColor shouldBe c1
                val mc = cTable.getMarkedAttr(c1)
                mc.shouldNotBeNull()
                mc.attr.attrValue shouldBe c1
                mc.refCount shouldBe 1
            }
        }

        test("set cell B1 text colo to $c1") {
            preCondition {
                sc.getCellState(cellB1Id)?.textFormat?.textColor.shouldBeNull()
            }

            action.setTextColor(cellB1Id, c1)

            postCondition {
                sc.getCellState(cellB1Id)?.textFormat?.textColor shouldBe c1
                val mc = cTable.getMarkedAttr(c1)
                mc.shouldNotBeNull()
                mc.attr.attrValue shouldBe c1
                mc.refCount shouldBe 2
            }
        }

        test("set cell A1 text colo to $c2") {

            action.setTextColor(cellA1Id, c2)

            postCondition {
                sc.getCellState(cellA1Id)?.textFormat?.textColor shouldBe c2
                val mc2 = cTable.getMarkedAttr(c2)
                mc2.shouldNotBeNull()
                mc2.attr.attrValue shouldBe c2
                mc2.refCount shouldBe 1

                val mc1 = cTable.getMarkedAttr(c1)
                mc1.shouldNotBeNull()
                mc1.attr.attrValue shouldBe c1
                mc1.refCount shouldBe 1
            }
        }

        test("set cell B1 text colo to $c2") {

            action.setTextColor(cellB1Id, c2)

            postCondition {
                sc.getCellState(cellB1Id)?.textFormat?.textColor shouldBe c2
                val mc2 = cTable.getMarkedAttr(c2)
                mc2.shouldNotBeNull()
                mc2.attr.attrValue shouldBe c2
                mc2.refCount shouldBe 2

                val mc1 = cTable.getMarkedAttr(c1)
                mc1.shouldBeNull()
            }
        }
    }

    @Test
    fun setTextSize() {
        val v1 = 123f
        test("Set cell A1 text size to 123") {
            preCondition {
                ffTable.getMarkedAttr(v1).shouldBeNull()
            }
            action.setTextSize(
                cellId = cellA1Id,
                textSize = v1,
            )
            postCondition {
                val attrMs = ffTable.getMarkedAttr(v1)
                attrMs.shouldNotBeNull()
                attrMs.refCount shouldBe 1
                sc.getCellStateMs(wbwsSt, cellA1Id.address)?.value?.textFormat.shouldNotBeNull()
            }
        }

        test("set cell B1 text size to 123") {
            action.setTextSize(
                cellId = cellB1Id,
                textSize = v1
            )
            postCondition {
                val attrMs = ffTable.getMarkedAttr(v1)
                attrMs.shouldNotBeNull()
                attrMs.refCount shouldBe 2
            }
        }

        val v2 = 456f
        test("Set cell A1 text size to 456") {
            preCondition {
                ffTable.getMarkedAttr(v2).shouldBeNull()
            }
            action.setTextSize(
                cellId = cellA1Id,
                textSize = v2,
            )
            postCondition {
                ffTable.getMarkedAttr(v2)?.refCount shouldBe 1
                ffTable.getMarkedAttr(v1)?.refCount shouldBe 1
            }
        }

        val v3 = 333f
        test("set B1 text size to 333") {
            preCondition {
                ffTable.getMarkedAttr(v3).shouldBeNull()
            }
            action.setTextSize(
                cellId = cellB1Id,
                textSize = v3,
            )
            postCondition {
                ffTable.getMarkedAttr(v3)?.refCount shouldBe 1
                ffTable.getMarkedAttr(v1).shouldBeNull()
            }
        }
    }
}
