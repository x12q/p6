package com.qxdzbc.common.copiers.binary_copier

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import kotlin.test.*

class BinaryCopierImpTest {

    @Test
    fun copy() {
        val copier = BinaryCopierImp()
        val o = "abc"
        val proto = o.toByteArray()
        copier.copy(proto)
        val cl: Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
        val binContent = cl.getData(BinaryTransferable.binFlavor) as ByteArray
        assertEquals(proto,binContent)
        assertEquals(o, String(binContent))
        val strContent = cl.getData(DataFlavor.stringFlavor)
        assertEquals(String(proto),strContent)
    }
}
