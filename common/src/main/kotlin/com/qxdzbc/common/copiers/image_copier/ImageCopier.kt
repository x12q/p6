package com.qxdzbc.common.copiers.image_copier

import com.qxdzbc.common.Rse
import java.awt.Image

/**
 * Copy an awt [Image] into the system clipboard
 */
interface ImageCopier {
    fun copy(image: Image):Rse<Unit>
}
