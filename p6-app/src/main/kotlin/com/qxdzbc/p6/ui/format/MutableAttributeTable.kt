package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import com.qxdzbc.p6.ui.format.pack.AttributePack
import com.qxdzbc.p6.ui.format.pack.ImmutableAttributePack

/**
 * AttributeTable is a 2d table of [AttributePack].
 * Each [AttributePack] is a list of [FormatAttribute].
 * To avoid duplicate [FormatAttribute], this table hold a set of [FormatAttribute],
 * then reference of these are distributed to each [AttributePack]
 */
class MutableAttributeTable(
    private val markedAttributeMap: MutableMap<FormatAttribute, MarkedAttribute> = mutableMapOf(),
    private var itable: TableCR<Int, Int, AttributePack> = ImmutableTableCR(),
) : AttributeTable {
    override val table: TableCR<Int, Int, AttributePack> get() = itable
    override val markedAttributes: Set<MarkedAttribute> get() = markedAttributeMap.values.toSet()

    /**
     * This function works like this:
     * -
     */
    override fun add(col: Int, row: Int, attr: FormatAttribute): AttributeTable {
        val inplaceAttr: MarkedAttribute? = markedAttributeMap[attr]
        val markedAttribute = if(inplaceAttr!=null){
            if(inplaceAttr.isValid){
                inplaceAttr
            }else{
                MarkedAttributes.valid(attr)
            }
        }else{
            MarkedAttributes.valid(attr)
        }

        // x: add the new marked attr to the internal map + up its counter by 1
        markedAttributeMap[attr] = markedAttribute.upCounter()

        //x: add the marked attr to the table
        val targetPack: AttributePack = table.getElement(col, row) ?: ImmutableAttributePack()
        val newPack: AttributePack = targetPack
            .removeInvalidAttribute()
            .add(markedAttribute)
        itable = table.set(col, row, newPack)
        return this
    }

    override fun add(cellAddress: CellAddress, attr: FormatAttribute): AttributeTable {
        return this.add(cellAddress.colIndex, cellAddress.rowIndex, attr)
    }

    /**
     * This function only marks the target attributes as invalid,
     * they will be deleted later by the getAttr method.
     * Immediate mass deletion will be very costly.
     */
    override fun removeAttrFromAllCell(attr: FormatAttribute): AttributeTable {
        markedAttributeMap[attr]?.invalidate()
        markedAttributeMap.remove(attr)
        return this
    }

    override fun removeOneAttrFromOneCell(col: Int, row: Int, attr: FormatAttribute): AttributeTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        if (targetPack != null) {
            val newPack: AttributePack = targetPack.remove(MarkedAttributes.wrap(attr))
            itable = table.set(col, row, newPack)
        }
        markedAttributeMap[attr]?.downCounter()
        return this
    }

    override fun removeAllAttrFromOneCell(col: Int, row: Int): AttributeTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        targetPack?.allAttrs?.forEach {
            markedAttributeMap[it]?.downCounter()
        }
        itable = table.remove(col, row)
        return this
    }

    override fun getAttr(col: Int, row: Int): FormatAttribute? {
        return this.getAttrPack(col, row)
    }

    /**
     * this function also remove all invalid attribute from the target pack
     */
    override fun getAttrPack(col: Int, row: Int): AttributePack? {
        val targetPack: AttributePack? = table.getElement(col, row)
        if (targetPack != null) {
            val cleanedPack = targetPack.removeInvalidAttribute()
            if (cleanedPack.isEmpty()) {
                itable = table.remove(col, row)
                return null
            } else {
                itable = table.set(col, row, cleanedPack)
                return cleanedPack
            }
        } else {
            return null
        }
    }
}
