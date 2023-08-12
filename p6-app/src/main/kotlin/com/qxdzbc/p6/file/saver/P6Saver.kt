package com.qxdzbc.p6.file.saver

import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.ui.workbook.state.CanConvertToWorkbookProto
import java.nio.file.Path

interface P6Saver {
    /**
     * Convert a workbook into a Protocol Buffers obj, then write it to a file
     */
    fun saveAsProtoBuf(wb: CanConvertToWorkbookProto, path:Path): Rse<Unit>

    /**
     * Save the first worksheet of a workbook as csv. Only the current values are extracted into a csv and saved to a file. No formula is saved.
     */
    fun saveFirstWsAsCsv(wb:Workbook, path:Path): Rse<Unit>
}
