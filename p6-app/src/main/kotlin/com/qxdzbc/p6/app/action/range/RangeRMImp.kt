package com.qxdzbc.p6.app.action.range

import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding

import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=RangeRM::class)
class RangeRMImp @Inject constructor(

    private val copyRangeToClipboardRM: CopyRangeToClipboardRM,

) : RangeRM, CopyRangeToClipboardRM by copyRangeToClipboardRM
