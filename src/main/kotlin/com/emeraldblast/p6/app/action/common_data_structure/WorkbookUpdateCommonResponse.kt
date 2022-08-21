package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.common.proto.ProtoUtils.toModel
import com.emeraldblast.p6.app.communication.event.WithP6EventLookupClazz
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookImp.Companion.toModel
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.WorkbookProtos.*
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString
import kotlin.reflect.KClass

open class WorkbookUpdateCommonResponse(
    override val errorReport: ErrorReport? = null,
    override val wbKey: WorkbookKey? = null,
    override val newWorkbook: Workbook? = null,
    override val windowId: String? = null,
) : ResponseWithWorkbookKeyTemplate, WorkbookUpdateCommonResponseInterface, WithP6EventLookupClazz {
    override val isError: Boolean get()=errorReport!=null
    companion object {
        fun fromProtoBytes(
            data: ByteString,
            translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>
        ): WorkbookUpdateCommonResponse {
            return WorkbookUpdateCommonResponseProto.newBuilder().mergeFrom(data).build().toModel(translatorGetter)
        }

        fun WorkbookUpdateCommonResponseProto.toModel(
            translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>
        ): WorkbookUpdateCommonResponse {
            return WorkbookUpdateCommonResponse(
                errorReport = if (this.hasErrorReport()) errorReport.toModel() else null,
                wbKey = if (this.hasWorkbookKey()) workbookKey.toModel() else null,
                newWorkbook = if (this.hasNewWorkbook()) newWorkbook.toModel(translatorGetter) else null,
                windowId = if (this.hasWindowId()) this.windowId else null,
            )
        }
    }

    override fun isLegal(): Boolean {
        return true
    }

    override val p6EventLookupClazz: KClass<out Any>
        get() = WorkbookUpdateCommonResponse::class
}
