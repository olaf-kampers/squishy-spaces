package com.olaf.squishyspaces.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.olaf.squishyspaces.data.model.Category
import com.olaf.squishyspaces.data.model.RoomAnalysis

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
        // Section title + image
        Text(
            text = "Room Analysis",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        AsyncImage(
            model = imageUri,
            contentDescription = "Analyzed room",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )

        // Hero score card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Large score
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${analysis.overallScore}",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor(analysis.overallScore),
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = " / 10",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Squishy reaction directly under score
                Text(
                    text = "${mood.emoji}  ${mood.reaction}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(12.dp))
                // Style guess below reaction
                Text(
                    text = analysis.styleGuess,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Top 3 metric cards
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

        // Quick Win card
        val topSuggestion = analysis.topSuggestions.firstOrNull()
        if (topSuggestion != null) {
            Text(
                text = "Quick Win",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "QUICK WIN (LOW COST)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.8.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = topSuggestion.suggestion,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(category: Category, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "${category.score} / 10",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = scoreColor(category.score),
            )
            LinearProgressIndicator(
                progress = { category.score / 10f },
                modifier = Modifier.fillMaxWidth(),
                color = scoreColor(category.score),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}
