package test.integration

import test.BaseAppStateTest
import kotlin.test.*

class CellFormulaIntegrationTest : BaseAppStateTest() {

    @Test
    fun buggyFormula_1(){
        test("the bug: input B1: in to a cell, it autocompletes into B1:<missing CELL_LIKE_ADDRESS>"){


        }
    }
}
