package com.emeraldblast.p6.ui.app.cell_editor.range_selector

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

data class RangeSelectorIdImp(
    override val windowId: String?,
    override val wbKeySt: St<WorkbookKey>?,
    override val wsNameSt: St<String>?
) : RangeSelectorId {
    override fun setWindowId(windowId: String?): RangeSelectorId {
        return this.copy(windowId=windowId)
    }

    override val wbKey: WorkbookKey?
        get() = wbKeySt?.value

    override fun setWbKeySt(wbKeySt: St<WorkbookKey>?): RangeSelectorId {
        return this.copy(wbKeySt = wbKeySt)
    }

    override val wsName: String?
        get() = wsNameSt?.value

    override fun setWsNameSt(wsNameSt: Ms<String>?): RangeSelectorId {
        return this.copy(wsNameSt = wsNameSt)
    }

}
