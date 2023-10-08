package com.qxdzbc.p6.composite_actions.worksheet.compute_slider_size

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.ColRowShifter
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import test.BaseAppStateTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class ComputeSliderSizeActionImpTest : BaseAppStateTest() {

    lateinit var action: ComputeSliderSizeActionImp

    @BeforeTest
    fun bt2() {
        action = ts.comp.computeSliderSizeActionImp()
    }



    @Test
    fun determineSliderSize() {
        val o = GridSliderImp(
            colRowShifter = ColRowShifter(
                visibleColRange = 1..5,
                visibleRowRange = 1..5,
            ),
            colLimit = WorksheetConstants.defaultColRange,
            rowLimit = WorksheetConstants.defaultRowRange,


            )
        val n: GridSlider = action.computeSliderProperties(
            oldGridSlider = o,
            availableSpace = DpSize(width = 100.dp, height = 200.dp),
            anchorCell = CellAddress(3, 2),
            getColWidth = { 30.dp },
            getRowHeight = { 20.dp }
        )
//        assertEquals(3 .. 6, n.visibleColRange)
        assertEquals(3..5, n.visibleColRangeIncludeMargin)
        assertEquals(2..11, n.visibleRowRangeIncludeMargin)
        assertNull(n.marginRow)
        assertEquals(6, n.marginCol)

        val n2: GridSlider = action.computeSliderProperties(
            o, DpSize(width = 100.dp, height = 200.dp),
            anchorCell = CellAddress(3, 2),
            getColWidth = { 30.dp },
            getRowHeight = { 30.dp }
        )
        assertEquals(3..5, n2.visibleColRangeIncludeMargin)
        assertEquals(2..7, n2.visibleRowRangeIncludeMargin)
        assertEquals(6, n2.marginCol)
        assertEquals(8, n2.marginRow)
    }


    @Test
    fun computeSliderSizeQQQ() {
        val o = GridSliderImp(
            colRowShifter = ColRowShifter(
                visibleColRange = 1..5,
                visibleRowRange = 1..5,
            ),
            colLimit = WorksheetConstants.defaultColRange,
            rowLimit = WorksheetConstants.defaultRowRange,
        )

        val ng = action.computeSliderProperties(
            oldGridSlider = o,
            availableSpace = DpSize(100.dp,100.dp),
            anchorCell = CellAddress(1,5),
            getColWidth = {
                10.dp
            },
            getRowHeight = {
                if(it == 5){
                    30.dp
                }else{
                    22.dp
                }
            }
        )
        println(ng)
        ng.visibleRowRangeIncludeMargin shouldBe (2 .. 6)
        ng.visibleColRangeIncludeMargin shouldBe (1 .. 10)
        ng.marginCol.shouldBeNull()
        ng.marginRow shouldBe 6

    }


    @Test
    fun `computeUp std case`(){
        val rt = action.computeUp(
            initIndex = 5,
            limitSize = 100.dp,
            getItemSize = {
                if(it==5){
                    30.dp
                }else{
                    22.dp
                }
            },
            checkIndexValidity = {it>0},
        )
        rt shouldBe ComputeSliderSizeActionImp.UpResult(2,5,null,96.dp)
    }


    @Test
    fun `computeUp only margin`(){
        val rt = action.computeUp(
            initIndex = 5,
            limitSize = 100.dp,
            getItemSize = {
                if(it==5){
                    3000.dp
                }else{
                    22.dp
                }
            },
            checkIndexValidity = {it>0},
        )
        rt shouldBe ComputeSliderSizeActionImp.UpResult(null,null,5,3000.dp)
    }

    @Test
    fun `computeDown only margin`(){
        val rt = action.computeDown(
            initIndex = 6,
            limitSize = 100.dp,
            initSize = 96.dp,
            getItemSize = {
                if(it==5){
                    30.dp
                }else{
                    22.dp
                }
            },
            checkIndexValidity = {it>0},
        )
        rt shouldBe ComputeSliderSizeActionImp.DownResult(null,null,6)
    }

    @Test
    fun `computeDown no margin`(){
        val rt = action.computeDown(
            initIndex = 6,
            limitSize = 100.dp,
            initSize = 70.dp,
            getItemSize = {
                10.dp
            },
            checkIndexValidity = {it>0},
        )
        rt shouldBe ComputeSliderSizeActionImp.DownResult(6,8,null)
    }

    @Test
    fun `computeDown std case`(){
        val rt = action.computeDown(
            initIndex = 3,
            limitSize = 100.dp,
            initSize = 22.dp,
            getItemSize = {
                if(it==5){
                    30.dp
                }else{
                    22.dp
                }
            },
            checkIndexValidity = {it>0},
        )
        rt shouldBe ComputeSliderSizeActionImp.DownResult(3,5,6)
    }


    @Test
    fun `computeTwoWay down side only has margin`(){
        val rs = action.computeTwoWay(
            initIndex = 5,
            limitSize = 100.dp,
            getItemSize = {
                if(it==5){
                    30.dp
                }else{
                    22.dp
                }
            },
            checkIndexValidity = {it>0},
        )

        rs shouldBe ComputeSliderSizeActionImp.TwoSideResult(2,5,6)
    }

    @Test
    fun `computeTwoWay std case`(){
        val rs = action.computeTwoWay(
            initIndex = 5,
            limitSize = 130.dp,
            getItemSize = {
                if(it==5){
                    30.dp
                }else{
                    22.dp
                }
            },
            checkIndexValidity = {it>1},
        )


        rs shouldBe ComputeSliderSizeActionImp.TwoSideResult(2,6,7)
    }


    @Test
    fun `computeTwoWay whole item and no margin`(){
        val rs = action.computeTwoWay(
            initIndex = 5,
            limitSize = 70.dp,
            getItemSize = {
                10.dp
            },
            checkIndexValidity = {it>1},
        )


        rs shouldBe ComputeSliderSizeActionImp.TwoSideResult(2,8,null)
    }


    @Test
    fun `create TwoSideResult`(){
        ComputeSliderSizeActionImp.TwoSideResult.from(
            upRs = ComputeSliderSizeActionImp.UpResult(1,10,null,0.dp),
            downRs = ComputeSliderSizeActionImp.DownResult(11,15,16),
        ) shouldBe ComputeSliderSizeActionImp.TwoSideResult(
            fromIndex = 1, toIndex = 15, margin = 16
        )

        ComputeSliderSizeActionImp.TwoSideResult.from(
            upRs = ComputeSliderSizeActionImp.UpResult(1,10,null,0.dp),
            downRs = ComputeSliderSizeActionImp.DownResult(11,15,null),
        ) shouldBe ComputeSliderSizeActionImp.TwoSideResult(
            fromIndex = 1, toIndex = 15, margin = null
        )

        ComputeSliderSizeActionImp.TwoSideResult.from(
            upRs = ComputeSliderSizeActionImp.UpResult(1,10,null,0.dp),
            downRs = ComputeSliderSizeActionImp.DownResult(11,null,null),
        ) shouldBe ComputeSliderSizeActionImp.TwoSideResult(
            fromIndex = 1, toIndex = 10, margin = null
        )
    }

}