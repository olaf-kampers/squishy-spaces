package com.olaf.squishyspaces.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.olaf.squishyspaces.R
import com.olaf.squishyspaces.data.model.Category
import com.olaf.squishyspaces.data.model.RoomAnalysis
import com.olaf.squishyspaces.ui.theme.SquishyDesign
import kotlinx.coroutines.delay

private val TOP_METRIC_NAMES = listOf("Layout", "Lighting", "Style Coherence")

@Composable
fun ResultOverviewTab(analysis: RoomAnalysis, imageUri: Uri) {
    val mood = moodFromScore(analysis.overallScore)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // ── Squishy hero card ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(SquishyDesign.RadiusHero), clip = false)
                .clip(RoundedCornerShape(SquishyDesign.RadiusHero)),
        ) {
            // Layer 1: scene background — clearly visible studio scene
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.82f),
                contentScale = ContentScale.Crop,
            )
            // Layer 2: scrim — slightly stronger now that there is no local frosted panel
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFF0A1520).copy(alpha = 0.28f)),
            )
            // Layer 3: soft brand gradient tint — colors the scene without burying it
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.36f)
                    .background(SquishyDesign.heroGradient),
            )

            // Decorative circles that bleed off the card edges
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-40).dp)
                    .background(Color.White.copy(alpha = 0.06f), CircleShape),
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-30).dp, y = 30.dp)
                    .background(Color.White.copy(alpha = 0.04f), CircleShape),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // ① Reaction + emoji — glass pill, primary focal text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A1520).copy(alpha = 0.34f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Text(
                            text = mood.reaction,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = SquishyDesign.OnHero,
                        )
                        Text(
                            text = mood.emoji,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                // ② Middle row: Squishy on the left, score on the right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Squishy with halo — no scrim, scene stays visible here
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.40f),
                                            Color.Transparent,
                                        ),
                                    ),
                                    shape = CircleShape,
                                ),
                        )
                        SquishyMascot(modifier = Modifier.size(136.dp))
                    }

                    // Score block — glass pill, score is the focal point
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFF0A1520).copy(alpha = 0.30f), RoundedCornerShape(12.dp))
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${analysis.overallScore}",
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Bold,
                                color = SquishyDesign.heroScoreColor(analysis.overallScore),
                            )
                            Text(
                                text = "/ 10",
                                style = MaterialTheme.typography.labelLarge,
                                color = SquishyDesign.OnHeroMuted,
                                modifier = Modifier.padding(start = 3.dp, bottom = 8.dp),
                            )
                        }
                    }
                }

                // ③ Style guess — glass pill, clearly tertiary
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A1520).copy(alpha = 0.26f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = analysis.styleGuess,
                        style = MaterialTheme.typography.bodySmall,
                        color = SquishyDesign.OnHeroSubtle,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        // ── Top Metrics ───────────────────────────────────────────────────
        Text(
            text = "Top Metrics",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TOP_METRIC_NAMES.forEach { name ->
                val category = analysis.categories.find { it.name == name }
                if (category != null) {
                    MetricCard(category = category, modifier = Modifier.weight(1f))
                }
            }
        }

        // ── Quick Win ─────────────────────────────────────────────────────
        val topSuggestion = analysis.topSuggestions.firstOrNull()
        if (topSuggestion != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(SquishyDesign.RadiusCard),
                colors = CardDefaults.cardColors(containerColor = SquishyDesign.AmberSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "💡  QUICK WIN",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SquishyDesign.AmberText,
                        letterSpacing = 0.8.sp,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = topSuggestion.suggestion,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        // ── Room image ────────────────────────────────────────────────────
        Text(
            text = "Your Room",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(SquishyDesign.RadiusCard), clip = false)
                .clip(RoundedCornerShape(SquishyDesign.RadiusCard)),
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Analyzed room",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
internal fun SquishyMascot(modifier: Modifier = Modifier) {
    var isBlinking by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            isBlinking = true
            delay(150)
            isBlinking = false
        }
    }

    Image(
        painter = painterResource(
            if (isBlinking) R.drawable.squishy_blink else R.drawable.squishy_pleased
        ),
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
private fun MetricCard(category: Category, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(SquishyDesign.RadiusCard),
        colors = CardDefaults.cardColors(containerColor = SquishyDesign.MintSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "${category.score} / 10",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = scoreColor(category.score),
            )
            LinearProgressIndicator(
                progress = { category.score / 10f },
                modifier = Modifier.fillMaxWidth().height(3.dp),
                color = scoreColor(category.score),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}
