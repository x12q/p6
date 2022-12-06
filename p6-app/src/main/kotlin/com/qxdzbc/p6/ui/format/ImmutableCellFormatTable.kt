package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import com.qxdzbc.p6.ui.format.pack.AttributePack
import com.qxdzbc.p6.ui.format.pack.ImmutableAttributePack
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * AttributeTable is a 2d table of [AttributePack].
 * Each [AttributePack] is a list of [FormatAttribute].
 * To avoid duplicate [FormatAttribute], this table hold a set of [FormatAttribute],
 * then reference of these are distributed to each [AttributePack]
 */
@ContributesBinding(scope=P6AnvilScope::class)
data class ImmutableCellFormatTable (
    private val markedAttributeMsMap: Map<FormatAttribute, Ms<MarkedAttribute>>,
    override val table: TableCR<Int, Int, AttributePack>,
) : CellFormatTable {

    @Inject
    constructor():this(mapOf(), ImmutableTableCR())

    override val markedAttributes: Set<MarkedAttribute> get() = markedAttributeMsMap.values.map { it.value }.toSet()

    /**
     * This function works like this:
     * -
     */
    override fun add(col: Int, row: Int, attr: FormatAttribute): ImmutableCellFormatTable {
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

    override fun add(cellAddress: CellAddress, attr: FormatAttribute): ImmutableCellFormatTable {
        return this.add(cellAddress.colIndex, cellAddress.rowIndex, attr)
    }

    private fun cleanUpMarkedAttrMap():ImmutableCellFormatTable{
        val newMap = markedAttributeMsMap.filter {
            val q = with(it.value.value){
                isCounterNotZero || isNotValid
            }
            q
        }
        return this.copy(markedAttributeMsMap = newMap)
    }

    override fun removeOneAttrFromOneCell(col: Int, row: Int, attr: FormatAttribute): ImmutableCellFormatTable {
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

    override fun removeAllAttrFromOneCell(col: Int, row: Int): CellFormatTable {
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
