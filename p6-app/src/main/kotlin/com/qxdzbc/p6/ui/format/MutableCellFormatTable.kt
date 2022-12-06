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
@Deprecated("dont use, kept just in case")
class MutableCellFormatTable(
    private val markedAttributeMap: MutableMap<FormatAttribute, MarkedAttribute> = mutableMapOf(),
    private var itable: TableCR<Int, Int, AttributePack> = ImmutableTableCR(),
) : CellFormatTable {
    override val table: TableCR<Int, Int, AttributePack> get() = itable
    override val markedAttributes: Set<MarkedAttribute> get() = markedAttributeMap.values.toSet()

    /**
     * This function works like this:
     * -
     */
    override fun add(col: Int, row: Int, attr: FormatAttribute): CellFormatTable {
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

    override fun add(cellAddress: CellAddress, attr: FormatAttribute): CellFormatTable {
        return this.add(cellAddress.colIndex, cellAddress.rowIndex, attr)
    }

    override fun removeOneAttrFromOneCell(col: Int, row: Int, attr: FormatAttribute): CellFormatTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        if (targetPack != null) {
            val newPack: AttributePack = targetPack.remove(MarkedAttributes.wrap(attr))
            itable = table.set(col, row, newPack)
        }
        markedAttributeMap[attr]?.downCounter()?.also {
            markedAttributeMap[attr] = it
        }
        cleanUpMarkedAttrMap()
        return this
    }

    private fun cleanUpMarkedAttrMap(){
        val newMap = markedAttributeMap.filter {
           with(it.value){
                isCounterNotZero || isNotValid
            }
        }
        newMap.keys.forEach{
            markedAttributeMap.remove(it)
        }
    }

    override fun removeAllAttrFromOneCell(col: Int, row: Int): CellFormatTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        targetPack?.allAttrs?.forEach {attr->
            markedAttributeMap[attr]?.downCounter()?.also {
                markedAttributeMap[attr] = it
            }
        }
        itable = table.remove(col, row)
        cleanUpMarkedAttrMap()
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
