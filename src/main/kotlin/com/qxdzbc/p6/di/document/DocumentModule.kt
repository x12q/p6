package com.qxdzbc.p6.di.document

import com.qxdzbc.p6.di.document.range.RangeModule

@dagger.Module(
    includes =  [
        RangeModule::class
    ]
)
interface DocumentModule {
}
