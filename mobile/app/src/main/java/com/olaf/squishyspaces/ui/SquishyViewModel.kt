package com.olaf.squishyspaces.ui

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaf.squishyspaces.data.api.SquishyApi
import com.olaf.squishyspaces.data.model.RoomAnalysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AppState {
    object Home : AppState()
    data class Preview(val uri: Uri) : AppState()
    object Loading : AppState()
    data class Result(val analysis: RoomAnalysis) : AppState()
    data class Error(val message: String) : AppState()
}

class SquishyViewModel : ViewModel() {

    private val _state = MutableStateFlow<AppState>(AppState.Home)
    val state: StateFlow<AppState> = _state

    private val _squidMode = MutableStateFlow("honest")
    val squidMode: StateFlow<String> = _squidMode

    fun setSquidMode(mode: String) {
        _squidMode.value = mode
    }

    fun onImageSelected(uri: Uri) {
        _state.value = AppState.Preview(uri)
    }

    fun onConfirmImage(uri: Uri, contentResolver: ContentResolver) {
        _state.value = AppState.Loading
        viewModelScope.launch {
            try {
                val result = SquishyApi.analyzeRoom(uri, contentResolver, _squidMode.value)
                _state.value = AppState.Result(result)
            } catch (e: Exception) {
                _state.value = AppState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun onReset() {
        _state.value = AppState.Home
    }
}
