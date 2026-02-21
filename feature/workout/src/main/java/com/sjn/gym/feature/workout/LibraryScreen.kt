package com.sjn.gym.feature.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Exercises", "Routines")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Library") })
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    // Exercises (reuse existing screen but wrap it appropriately)
                    // Assuming ExerciseListScreen handles its own data
                    ExerciseListScreen()
                }

                1 -> {
                    // Routines Placeholder
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("My Routines")
                        Text("Saved Splits")
                        // TODO: Implement Routine List
                    }
                }
            }
        }
    }
}
