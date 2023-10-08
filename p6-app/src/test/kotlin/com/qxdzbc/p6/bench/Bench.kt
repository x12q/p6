package com.qxdzbc.p6.bench

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.view.testApp
import test.TestSample
import java.util.UUID


/**
 * State hold data only
 */
interface MState{
    val x:Int
    val text:String

    val allText:String

    fun increaseX(i:Int):MState
    fun mutateText():MState
}

data class MStateImp(override val x:Int, override val text: String):MState {
    override val allText: String
        get() = "$text:$x"

    override fun increaseX(i: Int): MState {
        return this.copy(x=x+1)
    }

    override fun mutateText(): MState {
        return copy(text=UUID.randomUUID().toString())
    }
}

/**
 * action mirror the actual UI action or very specific action tied to UI event.
 * An action can change more than one state object at once.
 */
interface MAction{
    fun clickBtn()
}

class MActionImp(
    val stateMs:Ms<MState>
):MAction{
    val s by stateMs
    override fun clickBtn() {
        stateMs.value = s.increaseX(2).mutateText()
    }
}

/**
 * VM only act as an injection point
 */
class VM(
    val stateMs:Ms<MState>,
    val action:MAction
)

@Composable
fun MView(
    vm:VM
){
    MView(
        vm.stateMs.value,
        vm.action
    )
}

@Composable
fun MView(
    state:MState,
    action:MAction,
){
    Column {
        Text(state.x.toString())
        Text(state.text)
        Button({
            action.clickBtn()
        }){}
    }
}

fun main() {
    testApp {
        Row {
            Box(
                Modifier
                    .background(Color.Gray)
                    .fillMaxHeight()
                    .weight(1f)
            )
            Box(

                modifier = Modifier.background(Color.Red).size(20.dp)

            )
        }
    }
}


