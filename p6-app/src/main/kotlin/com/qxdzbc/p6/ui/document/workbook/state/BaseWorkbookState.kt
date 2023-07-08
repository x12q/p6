package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.p6.proto.DocProtos

/**
 * Contains derivative functions (that relies on other member functions) that does not need the actual implementation.
 */
abstract class BaseWorkbookState : WorkbookState{
    override fun toProto(): DocProtos.WorkbookProto {
        val proto1 = this.wb.makeSavableCopy().toProto()
        val rt = proto1.toBuilder()
            .clearWorksheet()
            .addAllWorksheet(
                this.worksheetStateList.map{it.toProto()}
            )
            .build()

        return rt
    }
}
