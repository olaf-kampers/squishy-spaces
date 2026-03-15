package com.olaf.squishyspaces.ui

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olaf.squishyspaces.data.api.SquishyApi
import com.olaf.squishyspaces.data.local.AnalysisHistoryStore
import com.olaf.squishyspaces.data.model.RoomAnalysis
import com.olaf.squishyspaces.data.model.SavedAnalysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class AppState {
    object Home : AppState()
    data class Preview(val uri: Uri) : AppState()
    object Loading : AppState()
    data class Result(val analysis: RoomAnalysis, val imageUri: Uri) : AppState()
    data class Error(val message: String) : AppState()
}

class SquishyViewModel(application: Application) : AndroidViewModel(application) {

    private val historyStore = AnalysisHistoryStore(application)

    private val _state = MutableStateFlow<AppState>(AppState.Home)
    val state: StateFlow<AppState> = _state

    private val _squidMode = MutableStateFlow("honest")
    val squidMode: StateFlow<String> = _squidMode

    private val _history = MutableStateFlow<List<SavedAnalysis>>(historyStore.load())
    val history: StateFlow<List<SavedAnalysis>> = _history

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
                saveAnalysis(uri, result)
                _state.value = AppState.Result(result, uri)
            } catch (e: Exception) {
                _state.value = AppState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun onHistoryItemSelected(saved: SavedAnalysis) {
        _state.value = AppState.Result(saved.analysis, Uri.parse(saved.imageUri))
    }

    fun onReset() {
        _state.value = AppState.Home
    }

    private fun saveAnalysis(uri: Uri, analysis: RoomAnalysis) {
        val item = SavedAnalysis(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            imageUri = uri.toString(),
            squidMode = _squidMode.value,
            analysis = analysis,
        )
        historyStore.save(item)
        _history.value = historyStore.load()
    }
}
