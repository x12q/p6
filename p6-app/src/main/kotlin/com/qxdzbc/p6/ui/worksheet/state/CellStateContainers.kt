package com.qxdzbc.p6.ui.worksheet.state

import com.qxdzbc.p6.common.table.TableCR
import com.qxdzbc.p6.common.table.CRTables
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.cell.state.CellState

typealias CellStateContainer = TableCR<Int, Int, Ms<CellState>>

object CellStateContainers{
    fun immutable():CellStateContainer{
        return CRTables.immutable()
    }

    fun mutable():CellStateContainer{
        return CRTables.mutable()
    }
}
