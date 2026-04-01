package com.sjn.gym.feature.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.model.Routine

@Composable
fun RoutineListScreen(
    onRoutineClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoutineListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is RoutineListUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is RoutineListUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                )
            }
            is RoutineListUiState.Success -> {
                if (state.routines.isEmpty()) {
                    EmptyRoutinesState(modifier = Modifier.align(Alignment.Center))
                } else {
                    RoutineList(routines = state.routines, onRoutineClick = onRoutineClick)
                }
            }
        }
    }
}

@Composable
fun RoutineList(
    routines: List<Routine>,
    onRoutineClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp), // Room for FAB or bottom nav if present
    ) {
        items(routines, key = { it.id }) { routine ->
            RoutineItem(routine = routine, onClick = { onRoutineClick(routine.id) })
            HorizontalDivider()
        }
    }
}

@Composable
fun RoutineItem(routine: Routine, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = routine.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent =
            if (routine.description.isNotEmpty() || routine.exercises.isNotEmpty()) {
                {
                    Column {
                        if (routine.description.isNotEmpty()) {
                            Text(
                                text = routine.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        if (routine.exercises.isNotEmpty()) {
                            Text(
                                text = "${routine.exercises.size} exercises",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                }
            } else null,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = modifier.clickable(onClick = onClick),
    )
}

@Composable
fun EmptyRoutinesState(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "No Routines Yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "Create a routine to start logging your workouts.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
