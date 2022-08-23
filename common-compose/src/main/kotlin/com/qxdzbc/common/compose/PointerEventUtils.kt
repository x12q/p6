package com.qxdzbc.common.compose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.*

object PointerEventUtils {

    fun PointerEvent.consumeAllChanges() {
        for (c in this.changes) {
            c.consumeAllChanges()
        }
    }

    fun PointerEvent.isNotAllConsumed(): Boolean {
        return !this.isAllConsumed()
    }
    @OptIn(ExperimentalComposeUiApi::class)
    fun PointerEvent.isAllConsumed(): Boolean {
        for (c in this.changes) {
            if (!c.isConsumed) {
                return false
            }
        }
        return true
    }

    fun PointerEvent.executeOnPressedThenConsumed(f:()->Unit){
        if(this.hasPressedChanges()){
            f()
            this.consumeAllPressed()
        }
    }

    fun PointerEvent.executeOnMoveThenConsumed(f:()->Unit){
        if(this.hasMoveChanges()){
            f()
            this.consumeAllMoved()
        }
    }

    fun PointerEvent.hasReleaseChanges(): Boolean {
        return this.changes.firstOrNull { it.changedToUp() } != null
    }


    fun PointerEvent.executeOnReleaseThenConsumed(f:()->Unit){
        if(this.hasReleaseChanges()){
            f()
            this.consumeAllRelease()
        }
    }

    fun PointerEvent.hasPressedChanges(): Boolean {
        return changes.firstOrNull { it.changedToDown() }!=null
    }

    fun PointerEvent.hasMoveChanges(): Boolean {
        for (c in this.changes) {
            if (c.positionChanged()) {
                return true
            }
        }
        return false
    }

    fun PointerEvent.consumeAllPressed() {
        for (c in this.changes) {
            if (c.changedToDown()) {
                c.consumeDownChange()
            }
        }
    }

    fun PointerEvent.consumeAllMoved() {
        for (c in this.changes) {
            if (c.positionChanged()) {
                c.consumePositionChange()
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun PointerEvent.consumeAllRelease(){
        for (c in changes){
            if(c.changedToUp()){
                c.consume()
            }
        }
    }
}
