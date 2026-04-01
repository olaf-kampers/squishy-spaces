package com.olaf.squishyspaces.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.olaf.squishyspaces.R

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

enum class SquishyMood(
    val emoji: String,
    val reaction: String,
    @DrawableRes val drawable: Int,
    val shouldBlink: Boolean,
) {
    DELIGHTED("🐙✨", "Squishy is genuinely impressed.",        R.drawable.squishy_happy,     shouldBlink = false),
    PLEASED  ("🐙",   "Squishy approves… mostly.",             R.drawable.squishy_pleased,   shouldBlink = true),
    SKEPTICAL("🐙🤨", "Squishy sees potential, but has notes.", R.drawable.squishy_thinking,  shouldBlink = false),
    UNIMPRESSED("🐙😐", "Squishy is trying to be polite.",     R.drawable.squishy_concerned, shouldBlink = false),
    HORRIFIED("🐙😱", "Squishy would like to leave this room immediately.", R.drawable.squishy_shocked, shouldBlink = false),
}

fun moodFromScore(score: Int): SquishyMood = when (score) {
    in 9..10 -> SquishyMood.DELIGHTED
    in 7..8  -> SquishyMood.PLEASED
    in 5..6  -> SquishyMood.SKEPTICAL
    in 3..4  -> SquishyMood.UNIMPRESSED
    else     -> SquishyMood.HORRIFIED
}
