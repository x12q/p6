package com.qxdzbc.p6.di.state.app_state

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import com.qxdzbc.common.flyweight.FlyweightTable
import com.qxdzbc.common.flyweight.FlyweightTableImp
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import dagger.Provides

@dagger.Module
interface CellFormatTableModule {
    companion object {
        @Provides
        fun TextVerticalAlignmentTable(): FlyweightTable<TextVerticalAlignment> {
            return FlyweightTableImp()
        }

        @Provides
        fun colorTable(): FlyweightTable<Color> {
            return FlyweightTableImp()
        }

        @Provides
        @P6Singleton
        fun CellFormatTableMs(i: CellFormatTableImp):Ms<CellFormatTable>{
            return StateUtils.ms(i)
        }

        @Provides
        fun AlignmentFormatTable(): FlyweightTable<TextHorizontalAlignment> {
            return FlyweightTableImp()
        }

        @Provides
        fun BoolFormatTable(): FlyweightTable<BoolAttr> {
            return FlyweightTableImp()
        }

        @Provides
        fun FloatFormatTable(): FlyweightTable<Float> {
            return FlyweightTableImp()
        }
    }
}

