package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.proto.WorksheetProtos

enum class LoadType {
    OVERWRITE,
    KEEP_OLD_DATA_IF_COLLIDE,
    UNDEFINED
    ;

    companion object {
        fun WorksheetProtos.LoadDataRequestProto.LoadTypeProto.toModel(): LoadType {
            return when (this) {
                WorksheetProtos.LoadDataRequestProto.LoadTypeProto.KEEP_OLD_DATA_IF_COLLIDE -> KEEP_OLD_DATA_IF_COLLIDE
                WorksheetProtos.LoadDataRequestProto.LoadTypeProto.OVERWRITE -> OVERWRITE
                else -> UNDEFINED
            }
        }
    }
}
