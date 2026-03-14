package com.sjn.gym.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun EditAgeDialog(initialAge: Int, onDismiss: () -> Unit, onSave: (Int) -> Unit) {
    var ageStr by remember { mutableStateOf(if (initialAge > 0) initialAge.toString() else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Age") },
        text = {
            OutlinedTextField(
                value = ageStr,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        ageStr = it
                    }
                },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            Button(onClick = { onSave(ageStr.toIntOrNull() ?: 0) }, enabled = ageStr.isNotEmpty()) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
fun EditGenderDialog(initialGender: String?, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var selectedGender by remember { mutableStateOf(initialGender ?: "MALE") }
    val options = listOf("MALE", "FEMALE", "OTHER")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Gender") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        androidx.compose.material3.RadioButton(
                            selected = selectedGender == option,
                            onClick = { selectedGender = option },
                        )
                        Text(
                            text = option.lowercase().replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onSave(selectedGender) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
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
            Column {
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
                    onOptionSelected = { weightUnit = WeightUnit.valueOf(it) },
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
            Column {
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
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
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
                Spacer(modifier = Modifier.height(16.dp))
                SegmentedButton(
                    options = listOf("CM", "FT"),
                    selectedOption = heightUnit.name,
                    onOptionSelected = {
                        heightUnit = HeightUnit.valueOf(it)
                        heightValue = ""
                        heightFeet = ""
                        heightInches = ""
                    },
                )
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
    var selectedLevel by remember { mutableStateOf(initialLevel ?: "BEGINNER") }
    val options = listOf("BEGINNER", "INTERMEDIATE", "ADVANCED")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Experience Level") },
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
                        Text(
                            text = option.lowercase().replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(start = 8.dp),
                        )
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
    val options = listOf("BARBELL", "DUMBBELL", "KETTLEBELL", "MACHINE", "CABLE", "BODYWEIGHT")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Equipment") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                options.forEach { option ->
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
                        Text(
                            text = option.lowercase().replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onSave(selectedEquipment) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
