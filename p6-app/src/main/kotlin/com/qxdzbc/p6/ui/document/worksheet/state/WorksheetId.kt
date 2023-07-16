package com.qxdzbc.p6.ui.document.worksheet.state

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto

/**
 * Contain a worksheet name, and a St<WorkbookKey>. With this I can pass a worksheet state around, and still know which workbook it belongs to. Without this, in order to find out that, I would need to scan through all the workbook, and match ws name, which is very unsafe in case multiple workbook having ws with the same name. So at the first glance, this [WorksheetId] seems redundant, but it actually serves a purpose.
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
    fun pointToWbKeySt(wbKey:St<WorkbookKey>):WorksheetId

    fun toProto(): WorksheetIdProto

    fun isSimilar(id:WorksheetId):Boolean
}
