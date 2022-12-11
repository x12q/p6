package com.qxdzbc.p6.ui.format.action

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
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

    fun produceNewStateForNewUnderlined(){
        test("set cell A1 underlined: true -> false"){
            val cellFormatTable = CellFormatTableImp().updateBoolTable(
                FormatTableImp<BoolAttr>()
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.FALSE)
                    .addOrUpdate(BoolAttr.FALSE)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextUnderlined(true),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewUnderlined(
                inputState = inputState,
                underlined = false
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextUnderlined(false),
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
        test("set cell A1 text color: $c1 -> $c2") {
            val cellFormatTable = CellFormatTableImp().updateColorTable(
                FormatTableImp<Color>()
                    .addOrUpdate(c1)
                    .addOrUpdate(c2)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextFormat(TextFormat.createDefaultTextFormat().setTextColor(c1)),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewTextColor(
                inputState = inputState,
                color=c2
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextFormat(TextFormat.createDefaultTextFormat().setTextColor(c2)),
                    inputState.cellFormatTable.updateColorTable(
                        inputState.cellFormatTable.colorTable
                            .reduceCountIfPossible(c1).addOrUpdate(c2)
                    )
                )
                newState shouldBe expectedState
            }
        }

    }

    @Test
    fun produceNewStateForNewTextSize() {
        val v1 = 123f
        val v2 = 456f
        val v3 = 333f

        test("set cell A1 text size: 123 -> 456") {
            val cellFormatTable = CellFormatTableImp().updateFloatTable(
                FormatTableImp<Float>()
                    .addOrUpdate(v1)
                    .addOrUpdate(v1)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextFormat(TextFormat.createDefaultTextFormat().setTextSize(v1)),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewTextSize(
                inputState = inputState,
                textSize = v2
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextFormat(TextFormat.createDefaultTextFormat().setTextSize(v2)),
                    inputState.cellFormatTable.updateFloatTable(
                        inputState.cellFormatTable.floatTable
                            .reduceCountIfPossible(v1).addOrUpdate(v2)
                    )
                )
                newState shouldBe expectedState
            }
        }



    }
}
