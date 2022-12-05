package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
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
data class ImmutableAttributeTable(
    private val markedAttributeMsMap: Map<FormatAttribute, Ms<MarkedAttribute>> = mapOf(),
    override val table: TableCR<Int, Int, AttributePack> = ImmutableTableCR(),
) : AttributeTable {

    override val markedAttributes: Set<MarkedAttribute> get() = markedAttributeMsMap.values.map { it.value }.toSet()

    /**
     * This function works like this:
     * -
     */
    override fun add(col: Int, row: Int, attr: FormatAttribute): AttributeTable {
        val inplaceAttrMs: Ms<MarkedAttribute>? = markedAttributeMsMap[attr]
        val markedAttributeMs = if (inplaceAttrMs != null) {
            inplaceAttrMs.value = inplaceAttrMs.value.validate()
            inplaceAttrMs
        } else {
            ms(MarkedAttributes.valid(attr))
        }

        // x: add the new marked attr to the internal map + up its counter by 1
        markedAttributeMs.value = markedAttributeMs.value.upCounter()
        val newMarkedAttributeMap = markedAttributeMsMap + (attr to markedAttributeMs)

        //x: add the marked attr to the table

        val newTable = run {
            val targetPack: AttributePack = table.getElement(col, row) ?: ImmutableAttributePack()
            val newPack: AttributePack = targetPack
                .removeInvalidAttribute()
                .add(markedAttributeMs)
            table.set(col, row, newPack)
        }

        return this.copy(
            markedAttributeMsMap = newMarkedAttributeMap,
            table = newTable
        )
    }

    override fun add(cellAddress: CellAddress, attr: FormatAttribute): AttributeTable {
        return this.add(cellAddress.colIndex, cellAddress.rowIndex, attr)
    }

    /**
     * TODO this does not clean up the table.
     * If I loop over the table to clean up all the attr pack, then what is the point of marked attr?
     * I need reactive programming here.
     */
    override fun removeAttrFromAllCell(attr: FormatAttribute): AttributeTable {
        markedAttributeMsMap[attr]?.also {
            it.value = it.value.invalidate()
        }
        val newMap = markedAttributeMsMap - attr
        return this.copy(
            markedAttributeMsMap = newMap
        ).cleanUpMarkedAttrMap()
    }

    private fun cleanUpMarkedAttrMap():ImmutableAttributeTable{
        val newMap = markedAttributeMsMap.filter {
            val q = with(it.value.value){
                isCounterNotZero || isNotValid
            }
            q
        }
        return this.copy(markedAttributeMsMap = newMap)
    }

    override fun removeOneAttrFromOneCell(col: Int, row: Int, attr: FormatAttribute): ImmutableAttributeTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        val rt =
            if (targetPack != null) {
                val newPack: AttributePack = targetPack.remove(attr)
                val newTable = if(newPack.isNotEmpty()){
                    table.set(col, row, newPack)
                }else{
                    table.remove(col, row)
                }
                markedAttributeMsMap[attr]?.also {
                    it.value = it.value.downCounter()
                }
                this.copy(table = newTable)
            } else {
                this
            }
        return rt.cleanUpMarkedAttrMap()
    }

    override fun removeAllAttrFromOneCell(col: Int, row: Int): AttributeTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        targetPack?.allAttrs?.forEach {
            this.markedAttributeMsMap[it]?.also { mAttrMs->
                mAttrMs.value = mAttrMs.value.downCounter()
            }
        }

        val newTable = table.remove(col, row)
        val rt=this.copy(table = newTable).cleanUpMarkedAttrMap()
        return rt
    }

    override fun getAttr(col: Int, row: Int): FormatAttribute? {
        return this.getAttrPack(col, row)
    }

    override fun getAttrPack(col: Int, row: Int): AttributePack? {
        val rt: AttributePack? = table.getElement(col, row)
        return rt
    }
}
