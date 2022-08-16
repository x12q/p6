package com.emeraldblast.p6.app.action.applier

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.emeraldblast.p6.app.communication.res_req_template.response.ScriptResponse
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Event

/**
 * This interface defines an applier that handle errors in responses of certain interface templates
 */
interface ErrorApplier {

    fun <R : ScriptResponse> applyScriptRes(
        res: R,
        onOk: (R) -> Unit,
    )

    fun <R : ResponseWithWindowIdAndWorkbookKey> apply3(
        res: R,
        event: P6Event,
        onOk: (R) -> Unit
    )

    fun <R : ResponseWithWindowIdAndWorkbookKey> apply3(
        res: R,
        onOk: (R) -> Unit
    )

    fun <R : ResponseWithWorkbookKeyTemplate> apply2(
        res: R,
        event: P6Event,
        onOk: (R) -> Unit
    )

    fun <R : ResponseWithWorkbookKeyTemplate> apply2(
        res: R,
        onOk: (R) -> Unit
    )




    fun <R : ScriptResponse> applyScriptResRs(
        res: R,
    ): Rse<R>

    fun <R : ResponseWithWindowIdAndWorkbookKey> apply3Rs(
        res: R,
        event: P6Event,
    ): Rse<R>

    fun <R : ResponseWithWindowIdAndWorkbookKey> apply3Rs(
        res: R,
    ): Rse<R>


    fun <R : ResponseWithWorkbookKeyTemplate> apply2Rs(
        res: R,
        event: P6Event,
    ):Rse<R>

    fun <R : ResponseWithWorkbookKeyTemplate> apply2Rs(
        res: R,
    ):Rse<R>

}
