package org.ovi.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(){

    fun execute(dispatcher : CoroutineContext = Dispatchers.Main,job: suspend () -> Unit) =
        viewModelScope.launch(dispatcher) {
            job.invoke()
        }


    fun ignoreCoroutineException(throwable: Throwable, callback: () -> Unit){
        if(throwable.message?.contains("Standalone") != true)
            callback.invoke()
    }

}