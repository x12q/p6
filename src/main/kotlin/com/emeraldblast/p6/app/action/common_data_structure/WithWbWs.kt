package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

interface WithWbWs{
    val wbKey: WorkbookKey
    val wsName:String
}

interface WithWbWsSt:WithWbWs{
    val wbKeySt: St<WorkbookKey>
    val wsNameSt:St<String>
    override val wbKey: WorkbookKey
        get() = wbKeySt.value
    override val wsName: String
        get() = wsNameSt.value
}

interface WithWbWsMs:WithWbWs{
    val wbKeyMs: Ms<WorkbookKey>
    val wsNameMs:Ms<String>
    override val wbKey: WorkbookKey get()=wbKeyMs.value
    override val wsName: String get()=wsNameMs.value
}
