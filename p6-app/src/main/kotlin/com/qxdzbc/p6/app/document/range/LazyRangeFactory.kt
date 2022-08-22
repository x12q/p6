package com.qxdzbc.p6.app.document.range

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.compose.St
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface LazyRangeFactory {
    fun create(
        @Assisted("1") address: RangeAddress,
        @Assisted("2") wsNameSt: St<String>,
        @Assisted("3") wbKeySt: St<WorkbookKey>,
    ): LazyRange
}
