package com.emeraldblast.p6.ui.document.worksheet.state

import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

/**
 * ID of a worksheet state, showing which worksheet the state is for
 */
interface WorksheetStateId: WithWbWs {
    val wbKeyMs:St<WorkbookKey>
    val wsNameMs:St<String>

    /**
     * Point this id to a different ws
     */
    fun pointToWsNameMs(wsNameMs:St<String>): WorksheetStateId
    /**
     * Point this id to a different wb
     */
    fun pointToWbKeyMs(wbKeyMs:St<WorkbookKey>):WorksheetStateId
}
