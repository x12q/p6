package com.qxdzbc.p6.ui.document.worksheet.state

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.proto.WorksheetProtos.WorksheetIdProto

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

    fun toProto():WorksheetIdProto
}
