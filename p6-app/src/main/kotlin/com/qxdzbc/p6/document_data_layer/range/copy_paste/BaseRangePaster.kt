package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.common.copiers.binary_copier.BinaryTransferable
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.range.RangeCopy
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import java.awt.Toolkit

abstract class BaseRangePaster : RangePaster {

    abstract val stateCont: StateContainer
    abstract val transCont: TranslatorContainer
    /**
     * extract the range copy from the clipboard
     */
    override fun readDataFromClipboard(wbKey: WorkbookKey, wsName: String): RangeCopy? {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val wbwsSt: WbWsSt? = stateCont.getWbWsSt(wbKey, wsName)
        if(wbwsSt!=null){
            val translator = transCont.getTranslatorOrCreate(wbwsSt)
            val bytes = clipboard.getData(BinaryTransferable.binFlavor) as ByteArray
            val rangeCopy = RangeCopy.fromProtoBytes(bytes, translator)
            return rangeCopy
        }else{
            return null
        }
    }
}
