package com.qxdzbc.common.copiers.image_copier

import com.github.michaelbull.result.Err
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.CommonErrors
import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard


class ImageCopierImp constructor() : ImageCopier {
    override fun copy(image: Image): Rse<Unit> {
        try {
            val cl: Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
            val imageTransferable = ImageTransferable(image)
            cl.setContents(imageTransferable, null)
            return Unit.toOk()
        } catch (e: Throwable) {
            val err = CommonErrors.ExceptionError.report(e)
            return Err(err)
        }
    }
}
