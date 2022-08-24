package com.qxdzbc.common.copiers.binary_copier

import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result
/**
 * Copy a byte array into the system's clipboard
 */
interface BinaryCopier {
    fun copyRs(data:ByteArray):Result<ByteArray,ErrorReport>
    fun copy(data:ByteArray):ByteArray
}
