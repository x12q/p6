package com.qxdzbc.common.file_util

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import java.nio.file.Path

/**
 * Use this interface instead of java.nio directly so that file reading logic can be mocked and injected
 */
interface FileUtil {
    fun readString(path:Path): Rs<String?, ErrorReport>
}
