package com.olaf.squishyspaces.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olaf.squishyspaces.data.model.RoomAnalysis

@Composable
fun ResultDetailsTab(analysis: RoomAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // All categories
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

        // All suggestions
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
    }
}
