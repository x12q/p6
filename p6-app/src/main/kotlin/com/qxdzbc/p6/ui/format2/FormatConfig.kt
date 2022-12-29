package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment

data class FormatConfig(
    val textSizeConfig: FormatConfigSet<Float>,
    val textColorConfig: FormatConfigSet<Color>,
    val textUnderlinedConfig: FormatConfigSet<Boolean>,
    val textCrossedConfig: FormatConfigSet<Boolean>,
    val fontWeightConfig: FormatConfigSet<FontWeight>,
    val fontStyleConfig: FormatConfigSet<FontStyle>,
    val horizontalAlignmentConfig: FormatConfigSet<TextHorizontalAlignment>,
    val verticalAlignmentConfig: FormatConfigSet<TextVerticalAlignment>,
    val backgroundColorConfig: FormatConfigSet<Color>
) :Shiftable{
    val allRanges
        get() = textSizeConfig.allRanges +
                textColorConfig.allRanges +
                textUnderlinedConfig.allRanges +
                textCrossedConfig.allRanges +
                fontWeightConfig.allRanges +
                fontStyleConfig.allRanges +
                horizontalAlignmentConfig.allRanges +
                verticalAlignmentConfig.allRanges +
                backgroundColorConfig.allRanges

    fun empty():FormatConfig{
        return FormatConfig(
            textSizeConfig= textSizeConfig.empty(),
            textColorConfig= textColorConfig.empty(),
            textUnderlinedConfig= textUnderlinedConfig.empty(),
            textCrossedConfig= textCrossedConfig.empty(),
            fontWeightConfig= fontWeightConfig.empty(),
            fontStyleConfig= fontStyleConfig.empty(),
            horizontalAlignmentConfig= horizontalAlignmentConfig.empty(),
            verticalAlignmentConfig= verticalAlignmentConfig.empty(),
            backgroundColorConfig= backgroundColorConfig.empty(),
        )
    }


    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): FormatConfig {
        return FormatConfig(
            textSizeConfig= textSizeConfig.shift(oldAnchorCell, newAnchorCell),
            textColorConfig= textColorConfig.shift(oldAnchorCell, newAnchorCell),
            textUnderlinedConfig= textUnderlinedConfig.shift(oldAnchorCell, newAnchorCell),
            textCrossedConfig= textCrossedConfig.shift(oldAnchorCell, newAnchorCell),
            fontWeightConfig= fontWeightConfig.shift(oldAnchorCell, newAnchorCell),
            fontStyleConfig= fontStyleConfig.shift(oldAnchorCell, newAnchorCell),
            horizontalAlignmentConfig= horizontalAlignmentConfig.shift(oldAnchorCell, newAnchorCell),
            verticalAlignmentConfig= verticalAlignmentConfig.shift(oldAnchorCell, newAnchorCell),
            backgroundColorConfig= backgroundColorConfig.shift(oldAnchorCell, newAnchorCell),
        )
    }

    companion object {
        fun empty(rangeAddress: RangeAddress):FormatConfig{
            return FormatConfig(
            textSizeConfig= FormatConfigSet.invalid(rangeAddress),
            textColorConfig= FormatConfigSet.invalid(rangeAddress),
            textUnderlinedConfig= FormatConfigSet.invalid(rangeAddress),
            textCrossedConfig= FormatConfigSet.invalid(rangeAddress),
            fontWeightConfig= FormatConfigSet.invalid(rangeAddress),
            fontStyleConfig= FormatConfigSet.invalid(rangeAddress),
            horizontalAlignmentConfig= FormatConfigSet.invalid(rangeAddress),
            verticalAlignmentConfig= FormatConfigSet.invalid(rangeAddress),
            backgroundColorConfig= FormatConfigSet.invalid(rangeAddress),
            )
        }
    }

}
