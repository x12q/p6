package com.emeraldblast.p6.app.communication.event.data_structure.range


import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.action.range.RangeId.Companion.toModel
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.proto.DocProtos.RangeIdProto
import kotlin.test.Test
import kotlin.test.assertEquals

class RangeIdTest {

    @Test
    fun toProto() {
        val m= RangeId(
            rangeAddress = RangeAddress("C1:J2"),
            wbKey = WorkbookKey("bb"),
            wsName = "QWE"
        )
        val p = m.toProto()
        assertEquals(m.rangeAddress.toProto(), p.rangeAddress)
        assertEquals(m.wbKey.toProto(),p.workbookKey)
        assertEquals(m.wsName,p.worksheetName)
    }

    @Test
    fun fromProto(){
        val m= RangeId(
            rangeAddress = RangeAddress("C1:J2"),
            wbKey = WorkbookKey("bb"),
            wsName = "QWE"
        )
        val p = RangeIdProto.newBuilder()
            .setRangeAddress(m.rangeAddress.toProto())
            .setWorkbookKey(m.wbKey.toProto())
            .setWorksheetName(m.wsName)
            .build()
        val m2 = p.toModel()
        assertEquals(m2.rangeAddress, m.rangeAddress)
        assertEquals(m2.wbKey, m.wbKey)
        assertEquals(m2.wsName, m.wsName)
    }
}
