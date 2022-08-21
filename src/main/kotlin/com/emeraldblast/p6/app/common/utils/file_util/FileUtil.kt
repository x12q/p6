package com.emeraldblast.p6.app.common.utils.file_util

import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.common.exception.error.ErrorReport
import java.nio.file.Path

/**
 * Use this interface instead of java.nio directly so that file reading logic can be mocked and injected
 */
interface FileUtil {
    fun readString(path:Path): Rs<String?, ErrorReport>
}
