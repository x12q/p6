package com.qxdzbc.common.copiers.binary_copier

import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard

class BinaryCopierImp constructor() : BinaryCopier {

    @kotlin.jvm.Throws(Exception::class)
    override fun copy(data: ByteArray): ByteArray {
        val rs = this.copyRs(data)
        return rs.getOrThrow()
    }

    override fun copyRs(data: ByteArray): Result<ByteArray, ErrorReport> {
        try {
            val cl: Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
            val dat = BinaryTransferable(data)
            cl.setContents(dat, null)
            return Ok(data)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }
}
