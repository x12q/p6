package com.qxdzbc.p6.app.action.common_data_structure

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.St

interface WbWs{
    val wbKey: WorkbookKey
    val wsName:String
}

fun WbWs(wbKey: WorkbookKey,
         wsName:String):WbWs{
    return WbWsImp(wbKey, wsName)
}

interface WbWsSt:WbWs{
    val wbKeySt: St<WorkbookKey>
    val wsNameSt:St<String>
    override val wbKey: WorkbookKey
        get() = wbKeySt.value
    override val wsName: String
        get() = wsNameSt.value
}

data class WbWsStImp(override val wbKeySt: St<WorkbookKey>, override val wsNameSt: St<String>):WbWsSt
fun WbWsSt(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>):WbWsSt{
    return WbWsStImp(wbKeySt, wsNameSt)
}
