package com.qxdzbc.p6.document_data_layer.workbook

import com.qxdzbc.common.compose.StateUtils.toMs

object Workbooks {

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
