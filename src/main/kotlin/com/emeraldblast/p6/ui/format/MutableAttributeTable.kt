package com.emeraldblast.p6.ui.format

import com.emeraldblast.p6.app.common.table.MutableTableCR
import com.emeraldblast.p6.ui.format.marked.MarkedAttribute
import com.emeraldblast.p6.ui.format.marked.MarkedAttributes
import com.emeraldblast.p6.ui.format.pack.AttributePack
import com.emeraldblast.p6.ui.format.pack.ImmutableAttributePack

/**
 * Why mutable table instead of immutable table?
 * AttributeTable is a 2d table of [AttributePack].
 * Each [AttributePack] is a list of [FormatAttribute].
 * To avoid duplicate [FormatAttribute], this table hold a set of [FormatAttribute],
 * then reference of these are distributed to each [AttributePack]
 */
class MutableAttributeTable(
    private val markedAttributeMap: MutableMap<FormatAttribute, MarkedAttribute> = mutableMapOf(),
    override val table: MutableTableCR<Int, Int, AttributePack> = MutableTableCR(),
) : AttributeTable {

    override val markedAttributes: Set<MarkedAttribute> get() = markedAttributeMap.values.toSet()

    override fun add(col: Int, row: Int, attr: FormatAttribute): AttributeTable {

        val markedAttr = MarkedAttributes.valid(attr)

        val inplaceAttr: MarkedAttribute? = markedAttributeMap[attr]
        val ma2: MarkedAttribute =
            if (inplaceAttr != null && inplaceAttr.isValid) {
                // x: use the in-place attr instead of the new one if in-place attr exists.
                inplaceAttr
            } else {
                // x: remove invalid attributes that are identical the new attr.
                // x: If I don't do it, the invalid will persist
                // x: inconsistency between the attribute sets, and the attribute in the table
                markedAttributeMap.remove(attr)
                markedAttr
            }

        // x: add the new marked attr to the internal map
        markedAttributeMap[attr] = ma2.upCounter()

        //x: add the marked attr to the table
        val targetPack: AttributePack = table.getElement(col, row) ?: ImmutableAttributePack()
        val newPack: AttributePack = targetPack
            .removeInvalidAttribute()
            .add(ma2)
        table.set(col, row, newPack)
        return this
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

    override fun removeAttrFromOneCell(col: Int, row: Int, attr: FormatAttribute): AttributeTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        if (targetPack != null) {
            val newPack: AttributePack = targetPack.remove(MarkedAttributes.wrap(attr))
            table.set(col, row, newPack)
        }
        markedAttributeMap[attr]?.downCounter()
        return this
    }

    override fun removeAllAttrAt(col: Int, row: Int): AttributeTable {
        val targetPack: AttributePack? = table.getElement(col, row)
        targetPack?.allAttrs?.forEach {
            markedAttributeMap[it]?.downCounter()
        }
        table.remove(col, row)
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
                table.remove(col, row)
                return null
            } else {
                table.set(col, row, cleanedPack)
                return cleanedPack
            }
        } else {
            return null
        }
    }
}
