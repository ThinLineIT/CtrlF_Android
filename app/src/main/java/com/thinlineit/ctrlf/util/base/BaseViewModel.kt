package com.thinlineit.ctrlf.util.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    fun CoroutineScope.loadingLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launch(context, start) {
            val loadingjob = launch {
                delay(500)
                isLoading.postValue(true)
            }
            try {
                block.invoke(this)
                loadingjob.cancel()
                isLoading.postValue(false)
            } catch (e: Exception) {
            }
        }
    }
}
