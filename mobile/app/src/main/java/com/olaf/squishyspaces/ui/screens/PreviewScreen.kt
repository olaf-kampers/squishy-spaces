package com.olaf.squishyspaces.ui.screens

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.olaf.squishyspaces.ui.SquishyViewModel

private data class ModeOption(val value: String, val label: String)

private val MODES = listOf(
    ModeOption("gentle", "Gentle 🫧"),
    ModeOption("honest", "Honest 🐙"),
    ModeOption("brutal", "Brutal 🦑"),
)

@Composable
fun PreviewScreen(uri: Uri, contentResolver: ContentResolver, viewModel: SquishyViewModel) {
    val squidMode by viewModel.squidMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Selected room photo",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "How honest should Squishy be?",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            MODES.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = squidMode == mode.value,
                    onClick = { viewModel.setSquidMode(mode.value) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = MODES.size),
                ) {
                    Text(mode.label)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = { viewModel.onReset() },
                modifier = Modifier.weight(1f),
            ) {
                Text("Back")
            }
            Button(
                onClick = { viewModel.onConfirmImage(uri, contentResolver) },
                modifier = Modifier.weight(1f),
            ) {
                Text("Analyze")
            }
        }
    }
}
