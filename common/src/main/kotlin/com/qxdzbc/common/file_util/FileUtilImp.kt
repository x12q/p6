package com.qxdzbc.common.file_util

import com.qxdzbc.common.error.CommonErrors
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import java.nio.file.Files
import java.nio.file.Path

class FileUtilImp constructor(): FileUtil {
    override fun readString(path: Path): Rs<String?, ErrorReport> {
        try{
            return Ok(Files.readString(path))
        }catch (e:Throwable){
            return CommonErrors.ExceptionError.report("Encounter exception when trying to read file:${path}",e).toErr()
        }
    }
}
