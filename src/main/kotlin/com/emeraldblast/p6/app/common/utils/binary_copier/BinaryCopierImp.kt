package com.emeraldblast.p6.app.common.utils.binary_copier

import com.emeraldblast.p6.app.common.utils.ErrorUtils.getOrThrow
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import javax.inject.Inject

class BinaryCopierImp @Inject constructor() : BinaryCopier {
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
