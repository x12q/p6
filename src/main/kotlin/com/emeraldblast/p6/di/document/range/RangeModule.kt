package com.emeraldblast.p6.di.document.range

import com.emeraldblast.p6.di.P6Singleton
import com.emeraldblast.p6.app.document.range.copy_paste.*
import dagger.Binds

@dagger.Module
interface RangeModule {
    @Binds
    @P6Singleton
    fun RangeCopier(i: RangeCopierImp): RangeCopier
    @Binds
    @P6Singleton
    fun RangePaster(i: RangePasterImp):RangePaster
}
