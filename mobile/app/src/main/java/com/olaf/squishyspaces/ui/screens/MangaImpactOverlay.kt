package com.olaf.squishyspaces.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MangaImpactOverlay(mode: ResultImpactMode, modifier: Modifier = Modifier) {
    when (mode) {
        ResultImpactMode.GREAT    -> GreatImpactOverlay(modifier)
        ResultImpactMode.DISASTER -> DisasterImpactOverlay(modifier)
        ResultImpactMode.MIXED, ResultImpactMode.NONE -> Unit
    }
}

@Composable
private fun GreatImpactOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Color.Black.copy(alpha = 0.48f)),
        contentAlignment = Alignment.Center,
    ) {
        // Radial gold glow from center
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD700).copy(alpha = 0.45f), Color.Transparent),
                    ),
                ),
        )
        // Manga speed lines
        SpeedLines(lineColor = Color(0xFFFFD700), modifier = Modifier.matchParentSize())
        // Impact content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            ImpactText(
                text = "AMAZING!",
                fillColor = Color(0xFFFFD700),
                strokeColor = Color.White,
            )
            Text(
                text = "Squishy is genuinely impressed ✨",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

@Composable
private fun DisasterImpactOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Color.Black.copy(alpha = 0.58f)),
        contentAlignment = Alignment.Center,
    ) {
        // Radial crimson glow from center
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFE53935).copy(alpha = 0.48f), Color.Transparent),
                    ),
                ),
        )
        // Manga speed lines
        SpeedLines(lineColor = Color(0xFFE53935), modifier = Modifier.matchParentSize())
        // Impact content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            ImpactText(
                text = "YIKES!!",
                fillColor = Color(0xFFFF5252),
                strokeColor = Color.White,
            )
            Text(
                text = "Squishy needs a moment... 😱",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

// Manga-style radiating speed lines drawn from the center outward
@Composable
private fun SpeedLines(lineColor: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val totalLines = 48
        val innerRadius = size.minDimension * 0.18f
        val outerRadius = size.maxDimension

        for (i in 0 until totalLines) {
            val angle = i * 2.0 * PI / totalLines
            val cosA = cos(angle).toFloat()
            val sinA = sin(angle).toFloat()
            val thick = i % 3 == 0
            drawLine(
                color = lineColor.copy(alpha = if (thick) 0.55f else 0.28f),
                start = Offset(cx + innerRadius * cosA, cy + innerRadius * sinA),
                end = Offset(cx + outerRadius * cosA, cy + outerRadius * sinA),
                strokeWidth = if (thick) 3f else 1.5f,
            )
        }
    }
}

// Two stacked Text composables: stroke layer below, fill layer on top
@Composable
private fun ImpactText(text: String, fillColor: Color, strokeColor: Color) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                drawStyle = Stroke(width = 12f, join = StrokeJoin.Round),
                color = strokeColor,
                textAlign = TextAlign.Center,
            ),
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                color = fillColor,
                textAlign = TextAlign.Center,
            ),
        )
    }
}
