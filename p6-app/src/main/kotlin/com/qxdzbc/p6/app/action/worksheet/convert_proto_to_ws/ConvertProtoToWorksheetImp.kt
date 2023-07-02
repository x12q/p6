package com.qxdzbc.p6.app.action.worksheet.convert_proto_to_ws

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellImp.Companion.toModel
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope


import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ConvertProtoToWorksheetImp @Inject constructor(
    val stateCont:StateContainer,
    val translatorContainer: TranslatorContainer,
) : ConvertProtoToWorksheet {

    private val sc = stateCont
    val tc = translatorContainer

    override fun convertProtoToWs(proto: DocProtos.WorksheetProto): Worksheet {
        val wbKey = proto.wbKey.toModel()
        val wsName = proto.name ?: ""
        val wsNameMs = sc.getWsNameMs(wbKey, wsName) ?: ms(proto.name)
        val wbKeyMs = sc.getWbKeyMs(wbKey) ?: ms(wbKey)
        var ws: Worksheet = WorksheetImp(
            nameMs = wsNameMs,
            table = ImmutableTableCR(),
            wbKeySt = wbKeyMs
        )
        val translator = tc.getTranslatorOrCreate(wbKeyMs, wsNameMs)

        for (cell: Cell in proto.cellsList.map { it.toModel(wbKeyMs, wsNameMs, translator) }) {
            ws = ws.addOrOverwrite(cell)
        }
        return ws
    }
}
