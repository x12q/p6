package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.proto.DocProtos.FormatConfigProto
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format2.FormatEntrySet.Companion.fontStyleToProto
import com.qxdzbc.p6.ui.format2.FormatEntrySet.Companion.fontWeightToProto
import com.qxdzbc.p6.ui.format2.FormatEntrySet.Companion.textHorizontalToProto
import com.qxdzbc.p6.ui.format2.FormatEntrySet.Companion.textVerticalToProto
import com.qxdzbc.p6.ui.format2.FormatEntrySet.Companion.toProto
import kotlin.random.Random

/**
 * A collection of multiple format entry sets in different categories.
 */
data class FormatConfig(
    val textSizeConfig: FormatEntrySet<Float> = FormatEntrySet(),
    val textColorConfig: FormatEntrySet<Color> = FormatEntrySet(),
    val textUnderlinedConfig: FormatEntrySet<Boolean> = FormatEntrySet(),
    val textCrossedConfig: FormatEntrySet<Boolean> = FormatEntrySet(),
    val fontWeightConfig: FormatEntrySet<FontWeight> = FormatEntrySet(),
    val fontStyleConfig: FormatEntrySet<FontStyle> = FormatEntrySet(),
    val horizontalAlignmentConfig: FormatEntrySet<TextHorizontalAlignment> = FormatEntrySet(),
    val verticalAlignmentConfig: FormatEntrySet<TextVerticalAlignment> = FormatEntrySet(),
    val backgroundColorConfig: FormatEntrySet<Color> = FormatEntrySet()
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

    fun toProto():FormatConfigProto{
        val rt = FormatConfigProto.newBuilder()
            .setTextSizeConfig(this.textSizeConfig.toProto())
            .setTextColorConfig(this.textColorConfig.toProto())
            .setTextUnderlinedConfig(textUnderlinedConfig.toProto())
            .setTextCrossedConfig(textCrossedConfig.toProto())
            .setFontWeightConfig(fontWeightConfig.fontWeightToProto())
            .setFontStyleConfig(fontStyleConfig.fontStyleToProto())
            .setHorizontalAlignmentConfig(horizontalAlignmentConfig.textHorizontalToProto())
            .setVerticalAlignmentConfig(verticalAlignmentConfig.textVerticalToProto())
            .setBackgroundColorConfig(backgroundColorConfig.toProto())
            .build()
        return rt
    }

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

    fun takeValid():FormatConfig{
        return FormatConfig(
            textSizeConfig = textSizeConfig.takeValid(),
            textColorConfig = textColorConfig.takeValid(),
            textUnderlinedConfig = textUnderlinedConfig.takeValid(),
            textCrossedConfig = textCrossedConfig.takeValid(),
            fontWeightConfig = fontWeightConfig.takeValid(),
            fontStyleConfig = fontStyleConfig.takeValid(),
            horizontalAlignmentConfig = horizontalAlignmentConfig.takeValid(),
            verticalAlignmentConfig = verticalAlignmentConfig.takeValid(),
            backgroundColorConfig = backgroundColorConfig.takeValid(),
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
                textSizeConfig = FormatEntrySet.invalid(rangeAddress),
                textColorConfig = FormatEntrySet.invalid(rangeAddress),
                textUnderlinedConfig = FormatEntrySet.invalid(rangeAddress),
                textCrossedConfig = FormatEntrySet.invalid(rangeAddress),
                fontWeightConfig = FormatEntrySet.invalid(rangeAddress),
                fontStyleConfig = FormatEntrySet.invalid(rangeAddress),
                horizontalAlignmentConfig = FormatEntrySet.invalid(rangeAddress),
                verticalAlignmentConfig = FormatEntrySet.invalid(rangeAddress),
                backgroundColorConfig = FormatEntrySet.invalid(rangeAddress),
            )
        }

        fun random(): FormatConfig {
            return FormatConfig(
                textSizeConfig = FormatEntrySet.random { Random.nextFloat() },
                textColorConfig = FormatEntrySet.random {
                    Color(Random.nextInt())
                },
                textUnderlinedConfig = FormatEntrySet.random {
                    Random.nextInt() % 2 == 0
                },
                textCrossedConfig = FormatEntrySet.random {
                    Random.nextInt() % 2 == 0
                },
                fontWeightConfig = FormatEntrySet.random {
                    listOf(FontWeight.Black, FontWeight.ExtraLight, FontWeight.Bold).random()
                },
                fontStyleConfig = FormatEntrySet.random {
                    FontStyle.values().random()
                },
                horizontalAlignmentConfig = FormatEntrySet.random {
                    TextHorizontalAlignment.random()
                },
                verticalAlignmentConfig = FormatEntrySet.random {
                    TextVerticalAlignment.random()
                },
                backgroundColorConfig = FormatEntrySet.random {
                    Color(Random.nextInt())
                },
            )
        }
    }
}
