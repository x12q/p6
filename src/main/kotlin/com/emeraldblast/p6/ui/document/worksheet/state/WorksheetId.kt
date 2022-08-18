package com.emeraldblast.p6.ui.document.worksheet.state

import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

/**
 * contain information that can be used to querying worksheets, worksheet states from the central app state
 */
interface WorksheetId: WbWs, WbWsSt {
    val wsNameMs:Ms<String>

    /**
     * Point this id to a different ws
     */
    fun pointToWsNameMs(wsNameMs: Ms<String>): WorksheetId
    /**
     * Point this id to a different wb
     */
    fun pointToWbKeySt(wbKeyMs:St<WorkbookKey>):WorksheetId
}
