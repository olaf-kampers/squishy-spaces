package com.olaf.squishyspaces.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.olaf.squishyspaces.ui.theme.SquishyDesign
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olaf.squishyspaces.data.model.RoomAnalysis
import com.olaf.squishyspaces.ui.SquishyViewModel

private val TABS = listOf("Overview", "Details")

@Composable
fun ResultScreen(analysis: RoomAnalysis, imageUri: Uri, viewModel: SquishyViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            TABS.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> ResultOverviewTab(analysis = analysis, imageUri = imageUri)
                1 -> ResultDetailsTab(analysis = analysis)
            }
        }

        Button(
            onClick = { viewModel.onReset() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SquishyDesign.TealAccent,
            ),
            shape = RoundedCornerShape(SquishyDesign.RadiusCard),
        ) {
            Text("Analyze another room", fontWeight = FontWeight.SemiBold)
        }
    }
}
