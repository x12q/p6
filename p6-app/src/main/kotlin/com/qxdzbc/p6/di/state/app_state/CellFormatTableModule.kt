package com.qxdzbc.p6.di.state.app_state

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import com.qxdzbc.p6.ui.format.FormatTable
import com.qxdzbc.p6.ui.format.FormatTableImp
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import dagger.Provides

@dagger.Module
interface CellFormatTableModule {
    companion object {
        @Provides
        fun colorTable():FormatTable<Color>{
            return FormatTableImp()
        }

        @Provides
        @P6Singleton
        fun CellFormatTableMs(i: CellFormatTableImp):Ms<CellFormatTable>{
            return StateUtils.ms(i)
        }

        @Provides
        fun AlignmentFormatTable(): FormatTable<TextHorizontalAlignment> {
            return FormatTableImp()
        }

        @Provides
        fun BoolFormatTable(): FormatTable<BoolAttr> {
            return FormatTableImp()
        }

        @Provides
        fun FloatFormatTable(): FormatTable<Float> {
            return FormatTableImp()
        }
    }
}

