package com.emeraldblast.p6.di.document

import com.emeraldblast.p6.di.document.range.RangeModule

@dagger.Module(
    includes =  [
        RangeModule::class
    ]
)
interface DocumentModule {
}
