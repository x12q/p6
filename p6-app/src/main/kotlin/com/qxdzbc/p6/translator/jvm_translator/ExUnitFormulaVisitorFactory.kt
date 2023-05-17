package com.qxdzbc.p6.translator.jvm_translator

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.St
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ExUnitFormulaVisitorFactory {
    fun create(
        @Assisted("1") wbKeySt: St<WorkbookKey>,
        @Assisted("2") wsNameSt: St<String>
    ): ExUnitFormulaVisitor
}
