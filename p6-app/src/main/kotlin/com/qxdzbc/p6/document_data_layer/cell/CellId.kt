package com.qxdzbc.p6.document_data_layer.cell

import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.toModel
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.CellIdProto
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM

data class CellId(
    val address: CellAddress,
    override val wbKeySt: St<WorkbookKey>,
    override val wsNameSt: St<String>
) : WbWsSt {

    constructor(address: CellAddress,wbwsSt:WbWsSt):this(address,wbwsSt.wbKeySt,wbwsSt.wsNameSt)

    companion object {
        /**
         *
         * a shallow model is a model that is not attached to the app state
         */
        fun CellIdProto.toShallowModel(): CellId {
            return CellId(
                address = this.cellAddress.toModel(),
                wbKeySt = this.wbKey.toModel().toSt(),
                wsNameSt = this.wsName.toSt()
            )
        }

        fun CellIdProto.toModelDM(): CellIdDM {
            return CellIdDM(
                address = this.cellAddress.toModel(),
                wbKey = this.wbKey.toModel(),
                wsName = this.wsName
            )
        }
    }

    fun repStr(): String {
        val pathStr = wbKey.path?.toAbsolutePath()?.let {
            "@'$it'"
        }?:""
        return "CellId(${address}@${wsName}@${wbKey.name}${pathStr}'"
    }

    fun isSimilar(another: CellId): Boolean {
        val sameAddress = this.address == another.address
        val similarWbKey = wbKey == another.wbKey
        val similarWsName = wsName == another.wsName
        return sameAddress && similarWbKey && similarWsName
    }

    fun setAddress(i: CellAddress): CellId {
        return this.copy(address = i)
    }

    fun toProto(): CellIdProto {
        return CellIdProto.newBuilder()
            .setCellAddress(address.toProto())
            .setWbKey(wbKey.toProto())
            .setWsName(wsName)
            .build()
    }

    fun toDm():CellIdDM{
        return CellIdDM(
            address = this.address,
            wbKey = wbKey,
            wsName = wsName
        )
    }
}
