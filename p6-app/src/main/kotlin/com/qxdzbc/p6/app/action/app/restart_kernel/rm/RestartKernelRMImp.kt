package com.qxdzbc.p6.app.action.app.restart_kernel.rm

import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator.Companion.toErrIndicator
import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelRequest
import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelResponse
import com.qxdzbc.p6.app.communication.res_req_template.RequestErrors

import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelConfigImp
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.message.api.connection.kernel_services.KernelServiceManager
import com.qxdzbc.p6.message.api.message.protocol.KernelConnectionFileContent
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapBoth
import com.google.gson.Gson
import com.qxdzbc.common.Rs
import com.qxdzbc.common.file_util.FileUtil
import com.qxdzbc.p6.di.state.app_state.MsKernelContextQualifier
import java.nio.file.Paths
import javax.inject.Inject

class RestartKernelRMImp @Inject constructor(
    @MsKernelContextQualifier
    private val kernelContext: KernelContext,
    private val kernelSM: KernelServiceManager,
    private val gson: Gson,
    private val fileUtil: FileUtil,
) : RestartKernelRM {
    override suspend fun restartKernel(request: RestartKernelRequest): RestartKernelResponse {
        if (request.isLegal()) {
            val r1 = restartWithKernelConfig(request)
            val rt = r1 ?: restartWithConnectionFileContent(request)
            return rt
        } else {
            return illegalStateResponse(request)
        }
    }

    private suspend fun restartWithKernelConfig(request: RestartKernelRequest): RestartKernelResponse? {
        if (request.pythonExecutablePath != null) {
            val kc = KernelConfigImp(
                launchCmd = listOf(request.pythonExecutablePath, "-m", "ipykernel_launcher", "-f"),
//                connectionFilePath = "connectionFile.json",
                connectionFilePath = "/tmp/phong-kernel4.json",
            )
            val rs: Rs<Unit, ErrorReport> = kernelSM.stopAll()
                .andThen { kernelContext.stopAll() }
                .andThen { kernelContext.startKernel(kc) }
                .andThen { kernelSM.startAll() }
            return RestartKernelResponse(
                errorIndicator = rs.toErrIndicator(),
                windowId = request.windowId
            )
        }
        return null
    }

    private suspend fun restartWithConnectionFileContent(request: RestartKernelRequest): RestartKernelResponse {
        try {
            val connectionFileContentRs = run {
                if (request.connectionFileJson != null) {
                    val c = gson.fromJson(request.connectionFileJson, KernelConnectionFileContent::class.java)
                    Ok(c)
                } else if (request.connectionFilePath != null) {
                    val jsonRs = fileUtil.readString(Paths.get(request.connectionFilePath))
                    val conFileRs = jsonRs.map {
                        if (it != null) {
                            val conFile = gson.fromJson(it, KernelConnectionFileContent::class.java)
                            conFile
                        } else {
                            null
                        }
                    }
                    conFileRs
                } else {
                    Ok(null)
                }
            }
            val rt: RestartKernelResponse = connectionFileContentRs.mapBoth(
                success = { connectionFileContent ->
                    if (connectionFileContent != null) {
                        val rs: Rs<Unit, ErrorReport> = kernelSM.stopAll()
                            .andThen { kernelContext.stopAll() }
                            .andThen { kernelContext.startKernel(connectionFileContent) }
                            .andThen { kernelSM.startAll() }
                        RestartKernelResponse(rs.toErrIndicator(), request.windowId)
                    } else {
                        illegalStateResponse(request)
                    }
                },
                failure = {
                    RestartKernelResponse(it.toErrIndicator(), request.windowId)
                }
            )
            return rt
        } catch (e: Throwable) {
            val errorIndicator = CommonErrors.ExceptionError.report(
                "Encounter exception when trying to restart kernel with connection file content:${e}", e
            ).toErrIndicator()
            return RestartKernelResponse(errorIndicator, request.windowId)
        }
    }

    private fun illegalStateResponse(request: RestartKernelRequest): RestartKernelResponse {

        return RestartKernelResponse(
            errorIndicator = RequestErrors.IllegalRequest.report(
                "Restart kernel request is in a illegal state. Exactly one of the properties must be valid.",
            ).toErrIndicator(),
            windowId = request.windowId
        )
    }
}
