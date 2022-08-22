package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardAction
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardActionImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface RangeActionModule {
    @Binds
    @P6Singleton
    fun RangeToClipboardAction(i:RangeToClipboardActionImp): RangeToClipboardAction
}
