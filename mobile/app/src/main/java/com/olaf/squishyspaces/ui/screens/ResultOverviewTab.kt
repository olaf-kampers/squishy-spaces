package com.olaf.squishyspaces.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
        // Squishy hero card — mascot-centric dashboard panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                SquishyMascot(modifier = Modifier.size(140.dp))
                Spacer(modifier = Modifier.height(20.dp))
                // Reaction as headline
                Text(
                    text = mood.reaction,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mood.emoji,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Score as secondary element
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${analysis.overallScore}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor(analysis.overallScore),
                    )
                    Text(
                        text = " / 10",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.55f),
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Style guess as tertiary label
                Text(
                    text = analysis.styleGuess,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
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
            Spacer(modifier = Modifier.height(4.dp))
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

        // Room image — below the verdict
        Text(
            text = "Your Room",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        AsyncImage(
            model = imageUri,
            contentDescription = "Analyzed room",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )

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
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp),
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
