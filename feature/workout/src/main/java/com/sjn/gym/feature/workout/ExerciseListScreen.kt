package com.sjn.gym.feature.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.model.Exercise

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseListScreen(
    modifier: Modifier = Modifier,
    viewModel: ExerciseListViewModel = hiltViewModel(),
    onExerciseClick: (String) -> Unit,
) {
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedEqFilter by viewModel.selectedEquipmentFilter.collectAsStateWithLifecycle()
    val selectedMusFilter by viewModel.selectedMuscleFilter.collectAsStateWithLifecycle()
    val availableEquipments by viewModel.availableEquipments.collectAsStateWithLifecycle()
    val availableMuscles by viewModel.availableMuscles.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Exercises",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            TextButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Create")
            }
        }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search Exercises") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(availableMuscles, key = { "muscle_$it" }) { muscle ->
                FilterChip(
                    selected = selectedMusFilter == muscle,
                    onClick = { viewModel.onMuscleFilterChange(muscle) },
                    label = { Text(muscle) },
                )
            }
            items(availableEquipments, key = { "eq_$it" }) { eq ->
                FilterChip(
                    selected = selectedEqFilter == eq,
                    onClick = { viewModel.onEquipmentFilterChange(eq) },
                    label = { Text(eq) },
                )
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(exercises, key = { it.id }) { exercise ->
                ExerciseCard(exercise = exercise, onClick = { onExerciseClick(exercise.id) })
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseCard(exercise: Exercise, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (exercise.targetMuscle.isNotEmpty()) {
                    AssistChip(
                        onClick = {},
                        label = { Text(exercise.targetMuscle) },
                        colors =
                            AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            ),
                        border = null,
                    )
                }
                exercise.secondaryMuscles.forEach { muscle ->
                    AssistChip(
                        onClick = {},
                        label = { Text(muscle) },
                        colors =
                            AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        border = null,
                    )
                }
            }
        }
    }
}
