package com.qxdzbc.p6.ui.kernel

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import java.nio.file.Path

interface KernelAction {
    suspend fun startKernelAndServices(pythonExecutablePath:String)
    suspend fun connectToKernelUsingConnectionFilePath(connectionFilePath: String)
    suspend fun connectToKernelUsingConnectionFileContent(connectionFileJson:String)
}