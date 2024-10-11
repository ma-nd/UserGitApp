package com.example.userrepo.base.ui

import androidx.lifecycle.ViewModel
import com.example.userrepo.base.data.Event
import com.example.userrepo.base.data.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel<T>(initialState: T) : ViewModel() {
    protected val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<T> by lazy { _uiState }

    private val _commonState = MutableStateFlow(CommonState())
    val commonState: StateFlow<CommonState> by lazy { _commonState }

    fun onError(error: Response.Error) {
        _commonState.value = _commonState.value.copy(error = Event(error))
    }

    fun closeScreen() {
        _commonState.value = _commonState.value.copy(closeScreen = Event(Unit))
    }

    data class CommonState(
        val error: Event<Response.Error>? = null,
        val closeScreen: Event<Unit>? = null
    )
}