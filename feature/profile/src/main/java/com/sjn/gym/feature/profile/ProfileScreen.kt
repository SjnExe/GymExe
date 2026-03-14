package com.sjn.gym.feature.profile

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sjn.gym.core.model.HeightUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    var showNameDialog by remember { mutableStateOf(false) }
    var showAgeDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }
    var showLevelDialog by remember { mutableStateOf(false) }
    var showEquipmentDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Personal Details
            ProfileSectionCard(title = "Personal Details") {
                ProfileItemRow(
                    label = "Name",
                    value = uiState.name.ifEmpty { "Not set" },
                    onClick = { showNameDialog = true },
                )
                Divider()
                ProfileItemRow(
                    label = "Age",
                    value = if (uiState.age > 0) "${uiState.age}" else "Not set",
                    onClick = { showAgeDialog = true },
                )
                Divider()
                ProfileItemRow(
                    label = "Gender",
                    value = uiState.gender ?: "Not set",
                    onClick = { showGenderDialog = true },
                )
            }

            // Health Details
            ProfileSectionCard(title = "Health Details") {
                ProfileItemRow(
                    label = "Weight",
                    value =
                        if (uiState.weightValue != null && uiState.weightValue!! > 0) {
                            "${uiState.weightValue} ${uiState.weightUnit.name}"
                        } else "Not set",
                    onClick = { showWeightDialog = true },
                )
                Divider()
                ProfileItemRow(
                    label = "Height",
                    value =
                        if (uiState.heightValue != null && uiState.heightValue!! > 0) {
                            if (uiState.heightUnit == HeightUnit.CM) {
                                "${uiState.heightValue} cm"
                            } else {
                                val totalInches = uiState.heightValue!! * 12
                                val feet = (totalInches / 12).toInt()
                                val inches = (totalInches % 12).toInt()
                                "$feet' $inches\""
                            }
                        } else "Not set",
                    onClick = { showHeightDialog = true },
                )
            }

            // Fitness Details
            ProfileSectionCard(title = "Fitness Details") {
                ProfileItemRow(
                    label = "Experience Level",
                    value = uiState.experienceLevel ?: "Not set",
                    onClick = { showLevelDialog = true },
                )
                Divider()
                ProfileItemRow(
                    label = "Equipment",
                    value =
                        if (uiState.equipmentList.isNotEmpty()) {
                            "${uiState.equipmentList.size} items"
                        } else "Not set",
                    onClick = { showEquipmentDialog = true },
                )
            }

            // Stats and History
            ProfileSectionCard(title = "Stats") {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("Weight History", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.weightValue != null && uiState.weightValue!! > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Current",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "${uiState.weightValue} ${uiState.weightUnit.name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Historical charts will appear here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        Text(
                            "No weight data available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Dialogs
        if (showNameDialog) {
            EditNameDialog(
                initialName = uiState.name,
                onDismiss = { showNameDialog = false },
                onSave = {
                    viewModel.setName(it)
                    showNameDialog = false
                },
            )
        }

        if (showAgeDialog) {
            EditAgeDialog(
                initialAge = uiState.age,
                onDismiss = { showAgeDialog = false },
                onSave = {
                    viewModel.setAge(it)
                    showAgeDialog = false
                },
            )
        }

        if (showGenderDialog) {
            EditGenderDialog(
                initialGender = uiState.gender,
                onDismiss = { showGenderDialog = false },
                onSave = {
                    viewModel.setGender(it)
                    showGenderDialog = false
                },
            )
        }

        if (showWeightDialog) {
            EditWeightDialog(
                initialValue = uiState.weightValue,
                initialUnit = uiState.weightUnit,
                onDismiss = { showWeightDialog = false },
                onSave = { value, unit ->
                    viewModel.setWeight(value, unit)
                    showWeightDialog = false
                },
            )
        }

        if (showHeightDialog) {
            EditHeightDialog(
                initialValue = uiState.heightValue,
                initialUnit = uiState.heightUnit,
                onDismiss = { showHeightDialog = false },
                onSave = { value, unit ->
                    viewModel.setHeight(value, unit)
                    showHeightDialog = false
                },
            )
        }

        if (showLevelDialog) {
            EditLevelDialog(
                initialLevel = uiState.experienceLevel,
                onDismiss = { showLevelDialog = false },
                onSave = {
                    viewModel.setExperienceLevel(it)
                    showLevelDialog = false
                },
            )
        }

        if (showEquipmentDialog) {
            EditEquipmentDialog(
                initialEquipment = uiState.equipmentList,
                onDismiss = { showEquipmentDialog = false },
                onSave = {
                    viewModel.setEquipmentList(it)
                    showEquipmentDialog = false
                },
            )
        }
    }
}

@Composable
fun ProfileSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
        )
        Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
            Column { content() }
        }
    }
}

@Composable
fun ProfileItemRow(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
