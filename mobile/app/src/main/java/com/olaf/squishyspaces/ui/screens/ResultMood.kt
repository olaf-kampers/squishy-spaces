package com.olaf.squishyspaces.ui.screens

import androidx.compose.ui.graphics.Color

// Shared score color used by both overview and details tabs
fun scoreColor(score: Int): Color = when (score) {
    in 1..3 -> Color(0xFFE53935)   // red
    in 4..6 -> Color(0xFFFFB300)   // amber
    in 7..8 -> Color(0xFF43A047)   // green
    else    -> Color(0xFF00C853)   // bright green
}

// Shared tier label used by details tab
fun tierLabel(tier: String): String = when (tier) {
    "low-cost"      -> "💡 LOW COST"
    "medium-effort" -> "🔧 MEDIUM EFFORT"
    "high-impact"   -> "✨ HIGH IMPACT"
    else            -> tier.uppercase()
}

enum class SquishyMood(val emoji: String, val reaction: String) {
    DELIGHTED("🐙✨", "Squishy is genuinely impressed."),
    PLEASED("🐙", "Squishy approves… mostly."),
    SKEPTICAL("🐙🤨", "Squishy sees potential, but has notes."),
    UNIMPRESSED("🐙😐", "Squishy is trying to be polite."),
    HORRIFIED("🐙😱", "Squishy would like to leave this room immediately."),
}

fun moodFromScore(score: Int): SquishyMood = when (score) {
    in 9..10 -> SquishyMood.DELIGHTED
    in 7..8  -> SquishyMood.PLEASED
    in 5..6  -> SquishyMood.SKEPTICAL
    in 3..4  -> SquishyMood.UNIMPRESSED
    else     -> SquishyMood.HORRIFIED
}
