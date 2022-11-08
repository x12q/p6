package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.CellIdProto

data class CellId(
    val address: CellAddress,
    override val wbKeySt: St<WorkbookKey>,
    override val wsNameSt: St<String>
) : WbWsSt {

    companion object {
        fun CellIdProto.toShallowModel(): CellId {
            return CellId(
                address = this.cellAddress.toModel(),
                wbKeySt = this.wbKey.toModel().toSt(),
                wsNameSt = this.wsName.toSt()
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
}
