package com.qxdzbc.p6.document_data_layer.range

import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.common.compose.St
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
