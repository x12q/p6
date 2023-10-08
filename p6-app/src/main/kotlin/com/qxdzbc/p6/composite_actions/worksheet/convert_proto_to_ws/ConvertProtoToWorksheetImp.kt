package com.qxdzbc.p6.composite_actions.worksheet.convert_proto_to_ws

import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.common.table.ImmutableTableCR
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.cell.CellImp.Companion.toModel
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.document_data_layer.worksheet.WorksheetImp
import com.qxdzbc.p6.di.P6AnvilScope


import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
            ws.addOrOverwrite(cell)
        }
        return ws
    }
}
