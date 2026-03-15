package com.olaf.squishyspaces.data.local

import android.content.Context
import com.olaf.squishyspaces.data.model.SavedAnalysis
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val PREFS_NAME = "analysis_history"
private const val KEY_ITEMS = "items"
private const val MAX_ITEMS = 10

class AnalysisHistoryStore(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun load(): List<SavedAnalysis> {
        val raw = prefs.getString(KEY_ITEMS, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<SavedAnalysis>>(raw)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun save(item: SavedAnalysis) {
        val current = load().toMutableList()

        // Skip back-to-back identical saves (same image, mode, and score)
        val last = current.firstOrNull()
        if (last != null &&
            last.imageUri == item.imageUri &&
            last.squidMode == item.squidMode &&
            last.analysis.overallScore == item.analysis.overallScore
        ) return

        current.add(0, item)
        prefs.edit()
            .putString(KEY_ITEMS, json.encodeToString(current.take(MAX_ITEMS)))
            .apply()
    }
}
