package com.emeraldblast.p6.app.document.workbook

import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.document.workbook.WorkbookImp.Companion.toModel
import com.emeraldblast.p6.proto.DocProtos.WorkbookProto
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.google.protobuf.ByteString

object Workbooks {
    fun fromProtoBytes(data:ByteString,translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>):Workbook{
        return WorkbookProto.newBuilder().mergeFrom(data).build().toModel(translatorGetter)
    }

    fun empty(name:String):Workbook{
        return WorkbookImp(keyMs = WorkbookKey(name).toMs())
    }
    fun empty(wbKey: WorkbookKey):Workbook{
        return WorkbookImp(keyMs = wbKey.toMs())
    }
    fun String?.isLegalWbName():Boolean{
        return !this.isNullOrEmpty()
    }
}
