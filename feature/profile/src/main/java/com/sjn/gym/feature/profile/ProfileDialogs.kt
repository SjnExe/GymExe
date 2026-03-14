package com.sjn.gym.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.time.LocalDate
import java.time.ZoneId

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
fun EditBirthDateDialog(initialBirthDate: Long, onDismiss: () -> Unit, onSave: (Long) -> Unit) {
    val initialDate =
        if (initialBirthDate > 0) {
            java.time.Instant.ofEpochMilli(initialBirthDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } else {
            LocalDate.now().minusYears(20) // default 20 years old
        }

    var year by remember { mutableStateOf(initialDate.year.toString()) }
    var month by remember { mutableStateOf(initialDate.monthValue.toString().padStart(2, '0')) }
    var day by remember { mutableStateOf(initialDate.dayOfMonth.toString().padStart(2, '0')) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Birth Date") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = year,
                    onValueChange = {
                        if (it.length <= 4 && it.all { c -> c.isDigit() }) year = it
                    },
                    label = { Text("YYYY") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1.5f),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = month,
                    onValueChange = {
                        if (it.length <= 2 && it.all { c -> c.isDigit() }) month = it
                    },
                    label = { Text("MM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = day,
                    onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) day = it },
                    label = { Text("DD") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    try {
                        val y = year.toInt()
                        val m = month.toInt()
                        val d = day.toInt()
                        val localDate = LocalDate.of(y, m, d)
                        val timestamp =
                            localDate
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                        onSave(timestamp)
                    } catch (e: Exception) {
                        // ignore invalid dates
                    }
                },
                enabled = year.length == 4 && month.isNotEmpty() && day.isNotEmpty(),
            ) {
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
