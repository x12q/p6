package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.copiers.binary_copier.BinaryTransferable
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.composite_actions.worksheet.paste_range.RangeCopyDM
import com.qxdzbc.p6.document_data_layer.range.RangeCopy
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import java.awt.Toolkit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class ClipboardReaderImp @Inject constructor(
    val stateCont:StateContainer,
    val transCont: TranslatorContainer
) : ClipboardReader {

    override fun readDataFromClipboard(wbKey: WorkbookKey, wsName: String): RangeCopy? {
        return readDataFromClipboardRs(wbKey, wsName).component1()
    }

    override fun readDataFromClipboardRs(wbKey: WorkbookKey, wsName: String): Rse<RangeCopy> {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val wbwsSt: WbWsSt? = stateCont.getWbWsSt(wbKey, wsName)
        if (wbwsSt != null) {
            try {
                val translator = transCont  .getTranslatorOrCreate(wbwsSt)
                val bytes = clipboard.getData(BinaryTransferable.binFlavor) as ByteArray
                val rangeCopy = RangeCopy.fromProtoBytes(bytes, translator)
                return rangeCopy.toOk()
            } catch (e: Exception) {
                return ClipboardReaderErrors.ExceptionWhileTryingToReadData.report(e).toErr()
            }
        } else {
            return ClipboardReaderErrors.InvalidWbWs.report("${wbKey} and ${wsName} do not point to valid state objects")
                .toErr()
        }
    }

    override fun readRangeCopyDMFromClipboard(): RangeCopyDM? {
        return readRangeCopyDMFromClipboardRs().component1()
    }

    override fun readRangeCopyDMFromClipboardRs(): Rse<RangeCopyDM> {
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val bytes = clipboard.getData(BinaryTransferable.binFlavor) as ByteArray
            val rangeCopy = RangeCopyDM.fromProtoBytes(bytes)
            return rangeCopy.toOk()
        } catch (e: Exception) {
            return ClipboardReaderErrors.ExceptionWhileTryingToReadData.report(e).toErr()
        }
    }
}
