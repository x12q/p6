package com.qxdzbc.common.copiers.binary_copier

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

/**
 * For copying binary data into awt clipboard
 */
class BinaryTransferable(val data:ByteArray) : Transferable {
    companion object{
        val binFlavor = DataFlavor("application/p6-binary","binary for p6")
        private val BIN = 0
        val supportedFlavors = arrayOf(binFlavor, DataFlavor.stringFlavor, DataFlavor.getTextPlainUnicodeFlavor())
    }
    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return supportedFlavors
    }
    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean {
        return flavor in supportedFlavors
    }

    override fun getTransferData(flavor: DataFlavor?): Any {
        if (flavor !in supportedFlavors){
            throw UnsupportedFlavorException(flavor)
        }
        if(flavor == supportedFlavors[BIN]){
            return data
        }else{
            return String(data, Charsets.UTF_8)
        }
    }
}
