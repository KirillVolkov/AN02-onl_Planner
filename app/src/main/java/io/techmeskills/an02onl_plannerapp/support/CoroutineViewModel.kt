package io.techmeskills.an02onl_plannerapp.support

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val viewModelJob = Job()

    override val coroutineContext: CoroutineContext =
        dispatcher + viewModelJob

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}