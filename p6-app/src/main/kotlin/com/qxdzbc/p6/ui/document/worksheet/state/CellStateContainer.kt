package com.qxdzbc.p6.ui.document.worksheet.state

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.common.table.CRTables
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.cell.state.CellState

typealias CellStateContainer = TableCR<Int, Int, Ms<CellState>>

object CellStateContainers{
    fun immutable():CellStateContainer{
        return CRTables.immutable()
    }

    fun mutable():CellStateContainer{
        return CRTables.mutable()
    }
}
