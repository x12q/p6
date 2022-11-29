package com.qxdzbc.p6.app.file.saver

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result
import java.nio.file.Path

interface P6Saver {
    /**
     * Convert a workbook into a Protocol Buffers obj, then write it to a file
     */
    fun saveAsProtoBuf(wb:Workbook, path:Path): Result<Unit, ErrorReport>

    /**
     * Save the first worksheet of a workbook as csv. Only the current values are extracted into a csv and saved to a file. No formula is saved.
     */
    fun saveFirstWsAsCsv(wb:Workbook, path:Path): Result<Unit, ErrorReport>
}
