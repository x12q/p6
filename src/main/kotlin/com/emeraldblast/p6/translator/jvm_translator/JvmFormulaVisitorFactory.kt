package com.emeraldblast.p6.translator.jvm_translator

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface JvmFormulaVisitorFactory{
    fun create(
        wbKey: WorkbookKey,
        worksheetName: String
    ): JvmFormulaVisitor
}
