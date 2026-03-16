package com.sjn.gym.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Woman
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.WeightUnit
import com.sjn.gym.core.ui.components.SegmentedButton
import java.time.LocalDate

@Composable
fun EditNameDialog(initialName: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var name by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Name") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = { Button(onClick = { onSave(name) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthDateDialog(
    initialBirthDate: Long,
    onDismiss: () -> Unit,
    onSave: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialMillis =
        if (initialBirthDate > 0) {
            initialBirthDate
        } else {
            java.time.LocalDate.now()
                .minusYears(20)
                .atStartOfDay(java.time.ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { datePickerState.selectedDateMillis?.let { onSave(it) } }) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun EditGenderDialog(initialGender: String?, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var selectedGender by remember { mutableStateOf(initialGender ?: "MALE") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Gender") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                GenderCard(
                    gender = "Male",
                    icon = Icons.Filled.Man,
                    isSelected = selectedGender == "MALE",
                    onClick = { selectedGender = "MALE" },
                    modifier = Modifier.weight(1f),
                )
                GenderCard(
                    gender = "Female",
                    icon = Icons.Filled.Woman,
                    isSelected = selectedGender == "FEMALE",
                    onClick = { selectedGender = "FEMALE" },
                    modifier = Modifier.weight(1f),
                )
            }
        },
        confirmButton = { Button(onClick = { onSave(selectedGender) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
fun GenderCard(
    gender: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.Card(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        colors =
            androidx.compose.material3.CardDefaults.cardColors(
                containerColor =
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,
                contentColor =
                    if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(imageVector = icon, contentDescription = gender, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = gender, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun EditWeightDialog(
    initialValue: Double?,
    initialUnit: WeightUnit,
    onDismiss: () -> Unit,
    onSave: (Double, WeightUnit) -> Unit,
) {
    var weightValue by remember { mutableStateOf(initialValue?.toString() ?: "") }
    var weightUnit by remember { mutableStateOf(initialUnit) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Weight") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = weightValue,
                    onValueChange = { newValue ->
                        if (
                            newValue.all { char -> char.isDigit() || char == '.' } &&
                                newValue.count { it == '.' } <= 1
                        ) {
                            val decimalIndex = newValue.indexOf('.')
                            if (decimalIndex == -1 || newValue.length - decimalIndex <= 3) {
                                weightValue = newValue
                            }
                        }
                    },
                    label = { Text("Weight") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                SegmentedButton(
                    options = listOf("KG", "LBS"),
                    selectedOption = weightUnit.name,
                    onOptionSelected = { newUnitStr ->
                        val newUnit = WeightUnit.valueOf(newUnitStr)
                        if (newUnit != weightUnit) {
                            weightValue.toDoubleOrNull()?.let { currentVal ->
                                val converted =
                                    if (newUnit == WeightUnit.LBS) {
                                        currentVal * 2.20462
                                    } else {
                                        currentVal * 0.453592
                                    }
                                weightValue = (Math.round(converted * 10.0) / 10.0).toString()
                            }
                            weightUnit = newUnit
                        }
                    },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dValue = weightValue.toDoubleOrNull()
                    if (dValue != null) onSave(dValue, weightUnit)
                },
                enabled = weightValue.toDoubleOrNull() != null,
            ) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
fun EditHeightDialog(
    initialValue: Double?,
    initialUnit: HeightUnit,
    onDismiss: () -> Unit,
    onSave: (Double, HeightUnit) -> Unit,
) {
    var heightValue by remember {
        mutableStateOf(if (initialUnit == HeightUnit.CM) initialValue?.toString() ?: "" else "")
    }
    var heightFeet by remember {
        mutableStateOf(
            if (initialUnit == HeightUnit.FT && initialValue != null)
                (initialValue).toInt().toString()
            else ""
        )
    }
    var heightInches by remember {
        mutableStateOf(
            if (initialUnit == HeightUnit.FT && initialValue != null)
                ((initialValue - initialValue.toInt()) * 12).toInt().toString()
            else ""
        )
    }
    var heightUnit by remember { mutableStateOf(initialUnit) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Height") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (heightUnit == HeightUnit.CM) {
                    OutlinedTextField(
                        value = heightValue,
                        onValueChange = { newValue ->
                            if (
                                newValue.all { char -> char.isDigit() || char == '.' } &&
                                    newValue.count { it == '.' } <= 1
                            ) {
                                val decimalIndex = newValue.indexOf('.')
                                if (decimalIndex == -1 || newValue.length - decimalIndex <= 2) {
                                    heightValue = newValue
                                }
                            }
                        },
                        label = { Text("Height (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        OutlinedTextField(
                            value = heightFeet,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                    heightFeet = newValue
                                }
                            },
                            label = { Text("Ft") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = heightInches,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                    val intVal = newValue.toIntOrNull()
                                    if (intVal == null || intVal < 12) {
                                        heightInches = newValue
                                    }
                                }
                            },
                            label = { Text("In") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                    }
                }

                Box(
                    modifier = Modifier.height(56.dp).padding(top = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    com.sjn.gym.core.ui.components.SegmentedButton(
                        options = listOf("CM", "FT"),
                        selectedOption = heightUnit.name,
                        onOptionSelected = { newUnitStr ->
                            val newUnit = HeightUnit.valueOf(newUnitStr)
                            if (newUnit != heightUnit) {
                                if (newUnit == HeightUnit.FT) {
                                    heightValue.toDoubleOrNull()?.let { cm ->
                                        val totalInches = cm / 2.54
                                        val feet = (totalInches / 12).toInt()
                                        val inches = Math.round(totalInches % 12).toInt()
                                        heightFeet = feet.toString()
                                        heightInches = inches.toString()
                                    }
                                } else {
                                    val f = heightFeet.toDoubleOrNull() ?: 0.0
                                    val i = heightInches.toDoubleOrNull() ?: 0.0
                                    val totalInches = (f * 12) + i
                                    if (totalInches > 0) {
                                        val cm = totalInches * 2.54
                                        heightValue = Math.round(cm).toString()
                                    }
                                }
                                heightUnit = newUnit
                            }
                        },
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (heightUnit == HeightUnit.CM) {
                        val dValue = heightValue.toDoubleOrNull()
                        if (dValue != null) onSave(dValue, heightUnit)
                    } else {
                        val feet = heightFeet.toDoubleOrNull() ?: 0.0
                        val inches = heightInches.toDoubleOrNull() ?: 0.0
                        val dValue = feet + (inches / 12.0)
                        if (dValue > 0) onSave(dValue, heightUnit)
                    }
                },
                enabled =
                    if (heightUnit == HeightUnit.CM) heightValue.toDoubleOrNull() != null
                    else (heightFeet.isNotEmpty() || heightInches.isNotEmpty()),
            ) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
fun EditLevelDialog(initialLevel: String?, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var selectedLevel by remember { mutableStateOf(initialLevel ?: "Freestyle") }
    val options = listOf("Freestyle", "Split Routine", "Full Body")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Training Style") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        androidx.compose.material3.RadioButton(
                            selected = selectedLevel == option,
                            onClick = { selectedLevel = option },
                        )
                        Text(text = option, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onSave(selectedLevel) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
fun EditEquipmentDialog(
    initialEquipment: Set<String>,
    onDismiss: () -> Unit,
    onSave: (Set<String>) -> Unit,
) {
    var selectedEquipment by remember { mutableStateOf(initialEquipment) }
    val options =
        listOf(
            "Dumbbells",
            "Barbell",
            "Kettlebell",
            "Pull-up Bar",
            "Cable Machine",
            "Leg Press",
            "Smith Machine",
            "Squat Rack",
            "Bench",
            "Medicine Ball",
            "Resistance Bands",
        )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Equipment") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(options) { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        androidx.compose.material3.Checkbox(
                            checked = selectedEquipment.contains(option),
                            onCheckedChange = { isChecked ->
                                val newSet = selectedEquipment.toMutableSet()
                                if (isChecked) newSet.add(option) else newSet.remove(option)
                                selectedEquipment = newSet
                            },
                        )
                        Text(text = option, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onSave(selectedEquipment) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
