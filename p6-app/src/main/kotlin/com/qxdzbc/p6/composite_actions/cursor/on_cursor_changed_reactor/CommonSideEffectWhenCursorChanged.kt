package com.qxdzbc.p6.composite_actions.cursor.on_cursor_changed_reactor

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt

/**
 * Invoke a set of common effects when a cursor changed. See implementation for detail of what effects are carried out.
 */
interface CommonSideEffectWhenCursorChanged {
    /**
     * Invoke a set of common effects when a cursor changed. See implementation for detail of what effects are carried out.
     */
    fun run(cursorId:WbWsSt)
}


