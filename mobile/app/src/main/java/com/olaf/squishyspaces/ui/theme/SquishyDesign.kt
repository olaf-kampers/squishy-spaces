package com.olaf.squishyspaces.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Lightweight design token object for the Squishy Spaces visual language.
 * Keep additions here minimal and intentional.
 */
object SquishyDesign {

    // Brand accents
    val TealAccent  = Color(0xFF2DBFA8)
    val LilacAccent = Color(0xFF7B6ED4)

    // Hero gradient: teal → lilac, renders diagonally across the composable bounds
    val heroGradient: Brush = Brush.linearGradient(
        colors = listOf(TealAccent, LilacAccent),
    )

    // Radial halo placed behind the mascot
    val haloGradient: Brush = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.30f),
            Color.Transparent,
        ),
    )

    // Card surfaces — intentionally lighter than the hero gradient
    val MintSurface  = Color(0xFFEBF9F6)  // metric cards
    val AmberSurface = Color(0xFFFFF7E6)  // quick win
    val AmberText    = Color(0xFF7A5200)  // text on amber surface

    // Text colors for use on gradient/dark hero backgrounds
    val OnHero       = Color.White
    val OnHeroMuted  = Color(0xCCFFFFFF)  // ~80% white
    val OnHeroSubtle = Color(0xA6FFFFFF)  // ~65% white

    // Corner radii
    val RadiusHero = 24.dp
    val RadiusCard = 16.dp

    // Score colors tuned for readability on the teal→lilac gradient
    fun heroScoreColor(score: Int): Color = when (score) {
        in 1..3 -> Color(0xFFFF8A80)  // soft red
        in 4..6 -> Color(0xFFFFD54F)  // gold
        in 7..8 -> Color(0xFF80FFCC)  // mint green
        else    -> Color(0xFFA5FF82)  // lime
    }
}
