package com.olaf.squishyspaces.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaf.squishyspaces.data.model.RoomAnalysis
import com.olaf.squishyspaces.ui.SquishyViewModel

private fun scoreColor(score: Int): Color = when (score) {
    in 1..3 -> Color(0xFFE53935)   // red
    in 4..6 -> Color(0xFFFFB300)   // amber
    in 7..8 -> Color(0xFF43A047)   // green
    else    -> Color(0xFF00C853)   // bright green
}

private fun tierLabel(tier: String): String = when (tier) {
    "low-cost"      -> "💡 LOW COST"
    "medium-effort" -> "🔧 MEDIUM EFFORT"
    "high-impact"   -> "✨ HIGH IMPACT"
    else            -> tier.uppercase()
}

@Composable
fun ResultScreen(analysis: RoomAnalysis, viewModel: SquishyViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Header
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = "${analysis.overallScore}",
                        fontSize = 64.sp,
                        color = scoreColor(analysis.overallScore),
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = " / 10",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 10.dp),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.styleGuess,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Category breakdown
        Text(text = "Breakdown", style = MaterialTheme.typography.titleMedium)
        analysis.categories.forEach { category ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Text(
                            text = "${category.score} / 10",
                            style = MaterialTheme.typography.labelLarge,
                            color = scoreColor(category.score),
                        )
                    }
                    LinearProgressIndicator(
                        progress = { category.score / 10f },
                        modifier = Modifier.fillMaxWidth(),
                        color = scoreColor(category.score),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    Text(
                        text = category.reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // Suggestions
        Text(text = "Suggestions", style = MaterialTheme.typography.titleMedium)
        analysis.topSuggestions.forEach { suggestion ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = tierLabel(suggestion.tier),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = suggestion.suggestion,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        // Confidence note
        Text(
            text = analysis.confidenceNote,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Button(
            onClick = { viewModel.onReset() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Analyze another room")
        }
    }
}
