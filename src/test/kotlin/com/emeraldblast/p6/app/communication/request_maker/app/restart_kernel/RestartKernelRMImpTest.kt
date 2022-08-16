package com.emeraldblast.p6.app.communication.request_maker.app.restart_kernel

import com.emeraldblast.p6.app.common.utils.FileUtil
import com.emeraldblast.p6.app.action.app.restart_kernel.rm.RestartKernelRMImp
import com.emeraldblast.p6.app.action.app.restart_kernel.RestartKernelRequest
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelConfig
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import com.emeraldblast.p6.message.api.connection.kernel_services.KernelServiceManager
import com.emeraldblast.p6.message.api.message.protocol.KernelConnectionFileContent
import com.github.michaelbull.result.Ok
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.nio.file.Path
import kotlin.test.*
class RestartKernelRMImpTest {

    val mockCF = KernelConnectionFileContent(
        shellPort = 1,
        iopubPort = 2,
        stdinPort = 3,
        controlPort = 3,
        heartBeatPort = 3,
        ip = "ip",
        key = "123",
        protocol = "qwe",
        signatureScheme = "q",
        kernelName = "kn"
    )

    @BeforeTest
    fun b(){

    }

    @Test
    fun `restartKernel with connect file path`() {
        val kernelContext = mock<KernelContext>{
            whenever(it.startKernel(any<KernelConfig>())) doReturn Ok(Unit)
            whenever(it.startKernel(any<KernelConnectionFileContent>())) doReturn Ok(Unit)
            whenever(it.stopAll()) doReturn Ok(Unit)
        }
        val kernelSM = mock<KernelServiceManager>{
            onBlocking { it.startAll() } doReturn Ok(Unit)
            whenever(it.stopAll()) doReturn Ok(Unit)
        }
        val fileReader = mock<FileUtil>{
            whenever(it.readString(any<Path>())) doReturn Ok(Gson().toJson(mockCF))
        }

        val okRequests = listOf(
            RestartKernelRequest(
                connectionFilePath = "abc"
            ) ,
            RestartKernelRequest(
                pythonExecutablePath = "abc"
            ),
            RestartKernelRequest(
                connectionFileJson = Gson().toJson(mockCF)
            )
        )
        val rm = RestartKernelRMImp(
            kernelContext = kernelContext,
            kernelSM=kernelSM,
            gson = Gson(),
            fileUtil=fileReader
        )
        for(req in okRequests){
            val o=runBlocking {
                rm.restartKernel(req)
            }
            assertTrue { o.isOk }
        }

        val failRequests = listOf(
            RestartKernelRequest(),
            RestartKernelRequest("",""),

        )
        for(req in failRequests){
            val o=runBlocking {
                rm.restartKernel(req)
            }
            assertTrue { o.isError }
        }
    }
}
