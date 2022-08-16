package com.emeraldblast.p6.di.request_maker

import com.emeraldblast.p6.app.action.range.RangeRM
import com.emeraldblast.p6.app.action.range.RangeRMImp
import com.emeraldblast.p6.app.action.range.paste_range.rm.FakePasteRangeRM
import com.emeraldblast.p6.app.action.range.paste_range.rm.PasteRangeRM
import com.emeraldblast.p6.app.action.range.paste_range.rm.PasteRangeRMImp
import com.emeraldblast.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRM
import com.emeraldblast.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRMImp
import com.emeraldblast.p6.app.action.range.range_to_clipboard.rm.FakeCopyRangeToClipboardRM
import com.emeraldblast.p6.di.Fake
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface RangeRMModule {
    @Binds
    @P6Singleton
    
    fun PasteRangeRMLocal(i: PasteRangeRMImp): PasteRangeRM

    @Binds
    @P6Singleton
    
    fun CopyRangeToClipboardRMLocal(i: CopyRangeToClipboardRMImp): CopyRangeToClipboardRM

    @Binds
    @P6Singleton
    @com.emeraldblast.p6.di.Fake
    fun FakePasteRangeRequestMaker(i: FakePasteRangeRM): PasteRangeRM

    @Binds
    @P6Singleton
    fun RangeRequestMaker(i: RangeRMImp): RangeRM

    @Binds
    @P6Singleton
    @com.emeraldblast.p6.di.Fake
    fun FakeRangeToClipboardRequestMaker(i: FakeCopyRangeToClipboardRM): CopyRangeToClipboardRM
}
