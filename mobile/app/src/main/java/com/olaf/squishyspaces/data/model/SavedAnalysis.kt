package com.olaf.squishyspaces.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedAnalysis(
    val id: String,
    val timestamp: Long,
    val imageUri: String,   // stored as string; Uri is not serializable
    val squidMode: String,
    val analysis: RoomAnalysis,
)
