package com.emeraldblast.p6.di.applier

import com.emeraldblast.p6.app.action.range.RangeApplier
import com.emeraldblast.p6.app.action.range.RangeApplierImp
import com.emeraldblast.p6.app.action.range.paste_range.applier.PasteRangeApplier
import com.emeraldblast.p6.app.action.range.paste_range.applier.PasteRangeApplierImp
import com.emeraldblast.p6.app.action.range.range_to_clipboard.applier.*
import com.emeraldblast.p6.di.Fake
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface RangeApplierModule {
    @Binds
    @P6Singleton
    fun RangeEventApplier(i: RangeApplierImp): RangeApplier

    @Binds
    @P6Singleton
    fun RangeToClipboardInternalApplier(i: RangeToClipboardInternalApplierImp): RangeToClipboardInternalApplier

    @Binds
    @P6Singleton
    fun RangeToClipboardApplier(i: RangeToClipboardApplierImp): RangeToClipboardApplier

    @Binds
    @P6Singleton
    @com.emeraldblast.p6.di.Fake
    fun FakeRangeToClipboardApplier(i: FakeRangeToClipboardApplier): RangeToClipboardApplier

    @Binds
    @P6Singleton
    fun PasteRangeApplier(i: PasteRangeApplierImp): PasteRangeApplier
}
