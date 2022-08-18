package com.emeraldblast.p6.app.coderunner

import com.emeraldblast.p6.app.app_context.AppContext
import com.emeraldblast.p6.app.common.utils.Loggers
import com.emeraldblast.p6.di.Username
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContextReadOnly
import com.emeraldblast.p6.message.api.message.protocol.data_interface_definition.Shell
import com.emeraldblast.p6.message.api.message.sender.composite.ExecuteResult
import com.emeraldblast.p6.message.api.message.sender.shell.ExecuteRequest
import com.github.michaelbull.result.*
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject


/**
 * Accept a piece of Python code, then execute it by sending an execution message to IPython
 */
class PythonCodeRunner @Inject constructor(
    @Username
    val userName:String,
    val kernelContext:KernelContextReadOnly
) : CodeRunner {

    override suspend fun run(code: String, dispatcher: CoroutineDispatcher): Result<String, ErrorReport> {
        val kernelContext: KernelContextReadOnly = kernelContext
        Loggers.scriptLogger.info(code)
        val senderRs = kernelContext.getSenderProvider().map { it.codeExecutionSender() }
        val rt2 = senderRs.mapBoth(
            success = { sender ->
                val message: ExecuteRequest = ExecuteRequest.autoCreate(
                    sessionId = kernelContext.getSession().unwrap().getSessionId(),
                    username = userName,
                    msgType = Shell.Execute.Request.msgType,
                    msgContent = Shell.Execute.Request.Content(
                        code = code,
                        silent = false,
                        storeHistory = true,
                        userExpressions = mapOf(),
                        allowStdin = false,
                        stopOnError = true
                    ),
                    kernelContext.getMsgIdGenerator().map { it.next() }.get() ?: UUID.randomUUID().toString()
                )
                val o:Result<ExecuteResult?,ErrorReport> = sender.send(message)


                val rt = if (o is Ok) {
                    Ok(o.get()?.content?.getTextPlain() ?: "")
                } else {
                    println(o.unwrapError())
                    Err(o.unwrapError())
                }
                rt
            },
            failure = {
                println(it)
                Err(it)
            }
        )
        return rt2
    }
}
