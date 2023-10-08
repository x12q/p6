package test

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.common.test_util.TestSplitter
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import java.lang.instrument.Instrumentation
import java.util.UUID
import kotlin.test.Test

 fun main(){
     val q = QWE()
     GlobalScope.launch {
         q.emitNumber()
     }
 }
// cold flow
fun <T> Flow<T>.throttleLatest(periodInMillis: Long): Flow<T> = channelFlow {
    var lastValue: T? = null
    var timerJob: Job? = null

    collect { value ->
        if (timerJob == null || !timerJob!!.isActive) {
            timerJob = launch {
                delay(periodInMillis)
                lastValue?.let { send(it) }
                lastValue = null
            }
        } else {
            lastValue = value
        }
    }

    awaitClose { timerJob?.cancel() }
}

class QWE{
    private val refresh = MutableStateFlow(0)
    private val scope = CoroutineScope(Dispatchers.Main) + Job()
    init {
        scope.launch {
            refresh.throttleLatest(1000)
                .collect{
                    println(it)
                }
        }
    }

    suspend fun emitNumber(){
        var x = 0
        while(true){
            refresh.emit(x)
            x+=1
            delay(300)
        }
    }
}


class TestBench :TestSplitter(){


    @Test
    fun q(){

    }

    @Test
    fun t() {

    }
}



