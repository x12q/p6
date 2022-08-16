package com.emeraldblast.p6.app.common.utils.binary_copier

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.proto.DocProtos
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import kotlin.test.*

class BinaryCopierImpTest {

    @Test
    fun copy() {
        val copier = BinaryCopierImp()
        val o = CellAddress("A1").toProto()
        val proto = o.toByteArray()
        copier.copy(proto)
        val cl: Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
        val binContent = cl.getData(BinaryTransferable.binFlavor) as ByteArray
        assertEquals(proto,binContent)
        assertEquals(o, DocProtos.CellAddressProto.newBuilder().mergeFrom(binContent).build())
        val strContent = cl.getData(DataFlavor.stringFlavor)
        assertEquals(String(proto),strContent)
    }
}
