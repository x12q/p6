package com.qxdzbc.p6.composite_actions.worksheet.paste_range

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.composite_actions.range.RangeIdDM.Companion.toModel
import com.qxdzbc.p6.document_data_layer.cell.CellImp.Companion.toModelDM
import com.qxdzbc.p6.proto.RangeProtos
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.ui.app.state.StateContainer

data class RangeCopyDM(
    val rangeId: RangeId,
    val cells:List<CellDM>
):WbWs by rangeId{
    companion object{

        fun fromProtoBytes(data: ByteArray):RangeCopyDM{
            return RangeProtos.RangeCopyProto.newBuilder().mergeFrom(data).build().toModelDM()
        }

        fun RangeProtos.RangeCopyProto.toModelDM(): RangeCopyDM {
            return RangeCopyDM(
                rangeId = this.id.toModel(),
                cells = this.cellList.map { it.toModelDM() }
            )
        }

        fun findRangeCopyInAppState(rangeId: RangeId, sc:StateContainer):RangeCopyDM?{
            val cells = sc.getWs(rangeId)?.getCellsInRange(rangeId.rangeAddress)?.map{it.toDm()}
            return cells?.let{RangeCopyDM(rangeId,it)}
        }
    }
}
