package com.olaf.squishyspaces.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomAnalysis(
    val overallScore: Int,
    val styleGuess: String,
    val categories: List<Category>,
    val topSuggestions: List<Suggestion>,
    val confidenceNote: String,
)

@Serializable
data class Category(
    val name: String,
    val score: Int,
    val reason: String,
)

@Serializable
data class Suggestion(
    val tier: String,
    val suggestion: String,
)
