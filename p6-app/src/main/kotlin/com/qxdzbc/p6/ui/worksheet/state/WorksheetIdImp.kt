package com.qxdzbc.p6.ui.worksheet.state

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto

data class WorksheetIdImp(
    override val wsNameMs: Ms<String>,
    override val wbKeySt: St<WorkbookKey>,
) : WorksheetId {
    override val wsName: String by wsNameMs
    override val wsNameSt: St<String>
        get() = wsNameMs
    override fun toProto(): WorksheetIdProto {
        return WorksheetIdProto.newBuilder()
            .setWsName(this.wsName)
            .setWbKey(this.wbKey.toProto())
            .build()
    }

    override fun isSimilar(id: WorksheetId): Boolean {
        return wsName == id.wsName && wbKey == id.wbKey
    }

    override fun pointToWsNameMs(wsNameMs: Ms<String>): WorksheetId {
        return this.copy(wsNameMs = wsNameMs)
    }

    override fun pointToWbKeySt(wbKey: St<WorkbookKey>): WorksheetId {
        return this.copy(wbKeySt = wbKey)
    }

    override val wbKey: WorkbookKey
        get() = wbKeySt.value
}
