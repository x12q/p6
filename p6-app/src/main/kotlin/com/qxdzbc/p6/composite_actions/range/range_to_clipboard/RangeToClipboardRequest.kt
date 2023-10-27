package com.qxdzbc.p6.composite_actions.range.range_to_clipboard

import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.composite_actions.range.RangeOperationRequest

class RangeToClipboardRequest(
    rangeId: RangeId,
    windowId: String?
) : RangeOperationRequest(rangeId, windowId)
