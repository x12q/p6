package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import kotlin.random.Random

/**
 * A collection of multiple format entry sets in different categories.
 */
data class FormatConfig(
    val textSizeConfig: FormatConfigEntrySet<Float> = FormatConfigEntrySet(),
    val textColorConfig: FormatConfigEntrySet<Color> = FormatConfigEntrySet(),
    val textUnderlinedConfig: FormatConfigEntrySet<Boolean> = FormatConfigEntrySet(),
    val textCrossedConfig: FormatConfigEntrySet<Boolean> = FormatConfigEntrySet(),
    val fontWeightConfig: FormatConfigEntrySet<FontWeight> = FormatConfigEntrySet(),
    val fontStyleConfig: FormatConfigEntrySet<FontStyle> = FormatConfigEntrySet(),
    val horizontalAlignmentConfig: FormatConfigEntrySet<TextHorizontalAlignment> = FormatConfigEntrySet(),
    val verticalAlignmentConfig: FormatConfigEntrySet<TextVerticalAlignment> = FormatConfigEntrySet(),
    val backgroundColorConfig: FormatConfigEntrySet<Color> = FormatConfigEntrySet()
) : Shiftable {
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

    /**
     * Empty this format config by nullify all format value, but keep the range address info in each config.
     */
    fun nullifyFormatValue(): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeConfig.nullifyFormatValue(),
            textColorConfig = textColorConfig.nullifyFormatValue(),
            textUnderlinedConfig = textUnderlinedConfig.nullifyFormatValue(),
            textCrossedConfig = textCrossedConfig.nullifyFormatValue(),
            fontWeightConfig = fontWeightConfig.nullifyFormatValue(),
            fontStyleConfig = fontStyleConfig.nullifyFormatValue(),
            horizontalAlignmentConfig = horizontalAlignmentConfig.nullifyFormatValue(),
            verticalAlignmentConfig = verticalAlignmentConfig.nullifyFormatValue(),
            backgroundColorConfig = backgroundColorConfig.nullifyFormatValue(),
        )
    }


    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeConfig.shift(oldAnchorCell, newAnchorCell),
            textColorConfig = textColorConfig.shift(oldAnchorCell, newAnchorCell),
            textUnderlinedConfig = textUnderlinedConfig.shift(oldAnchorCell, newAnchorCell),
            textCrossedConfig = textCrossedConfig.shift(oldAnchorCell, newAnchorCell),
            fontWeightConfig = fontWeightConfig.shift(oldAnchorCell, newAnchorCell),
            fontStyleConfig = fontStyleConfig.shift(oldAnchorCell, newAnchorCell),
            horizontalAlignmentConfig = horizontalAlignmentConfig.shift(oldAnchorCell, newAnchorCell),
            verticalAlignmentConfig = verticalAlignmentConfig.shift(oldAnchorCell, newAnchorCell),
            backgroundColorConfig = backgroundColorConfig.shift(oldAnchorCell, newAnchorCell),
        )
    }

    fun isInvalid():Boolean{
        return textColorConfig.isInvalid() &&
                textColorConfig.isInvalid() &&
                textUnderlinedConfig.isInvalid() &&
                textCrossedConfig.isInvalid() &&
                fontWeightConfig.isInvalid() &&
                fontStyleConfig.isInvalid() &&
                horizontalAlignmentConfig.isInvalid()&&
                verticalAlignmentConfig.isInvalid()&&
                backgroundColorConfig.isInvalid()
    }

    companion object {
        fun empty(rangeAddress: RangeAddress): FormatConfig {
            return FormatConfig(
                textSizeConfig = FormatConfigEntrySet.invalid(rangeAddress),
                textColorConfig = FormatConfigEntrySet.invalid(rangeAddress),
                textUnderlinedConfig = FormatConfigEntrySet.invalid(rangeAddress),
                textCrossedConfig = FormatConfigEntrySet.invalid(rangeAddress),
                fontWeightConfig = FormatConfigEntrySet.invalid(rangeAddress),
                fontStyleConfig = FormatConfigEntrySet.invalid(rangeAddress),
                horizontalAlignmentConfig = FormatConfigEntrySet.invalid(rangeAddress),
                verticalAlignmentConfig = FormatConfigEntrySet.invalid(rangeAddress),
                backgroundColorConfig = FormatConfigEntrySet.invalid(rangeAddress),
            )
        }

        fun random(): FormatConfig {
            return FormatConfig(
                textSizeConfig = FormatConfigEntrySet.random { Random.nextFloat() },
                textColorConfig = FormatConfigEntrySet.random {
                    Color(Random.nextInt())
                },
                textUnderlinedConfig = FormatConfigEntrySet.random {
                    Random.nextInt() % 2 == 0
                },
                textCrossedConfig = FormatConfigEntrySet.random {
                    Random.nextInt() % 2 == 0
                },
                fontWeightConfig = FormatConfigEntrySet.random {
                    listOf(FontWeight.Black, FontWeight.ExtraLight, FontWeight.Bold).random()
                },
                fontStyleConfig = FormatConfigEntrySet.random {
                    FontStyle.values().random()
                },
                horizontalAlignmentConfig = FormatConfigEntrySet.random {
                    TextHorizontalAlignment.random()
                },
                verticalAlignmentConfig = FormatConfigEntrySet.random {
                    TextVerticalAlignment.random()
                },
                backgroundColorConfig = FormatConfigEntrySet.random {
                    Color(Random.nextInt())
                },
            )
        }
    }
}
