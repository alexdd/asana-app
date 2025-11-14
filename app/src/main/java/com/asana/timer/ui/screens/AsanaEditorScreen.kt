package com.asana.timer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asana.timer.data.Asana
import com.asana.timer.data.AsanaSequence
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsanaEditorScreen(
    sequence: AsanaSequence?,
    onSave: (AsanaSequence) -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)?
) {
    val focusManager = LocalFocusManager.current
    val asanaItems = remember { mutableStateListOf<EditableAsana>() }
    var listName by remember { mutableStateOf(sequence?.name ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(sequence?.id) {
        asanaItems.clear()
        if (sequence == null) {
            asanaItems += EditableAsana()
        } else {
            asanaItems += sequence.asanas.map {
                EditableAsana(
                    id = it.id,
                    title = it.title,
                    duration = it.durationSeconds.toString(),
                    description = it.description
                )
            }
        }
    }

    Scaffold(
        topBar = {
            EditorTopBar(
                isEditing = sequence != null,
                onCancel = onCancel,
                onDelete = onDelete
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { asanaItems += EditableAsana() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Asana hinzufügen")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (sequence == null) "Neue Sequenz" else "Sequenz bearbeiten",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            OutlinedTextField(
                value = listName,
                onValueChange = { listName = it },
                label = { Text("Titel der Sequenz") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            asanaItems.forEachIndexed { index, editable ->
                AsanaEditorCard(
                    index = index,
                    state = editable,
                    onUpdate = { updated -> asanaItems[index] = updated },
                    onRemove = { if (asanaItems.size > 1) asanaItems.removeAt(index) },
                    canMoveUp = index > 0,
                    canMoveDown = index < asanaItems.lastIndex,
                    onMoveUp = {
                        if (index > 0) {
                            asanaItems.swap(index, index - 1)
                        }
                    },
                    onMoveDown = {
                        if (index < asanaItems.lastIndex) {
                            asanaItems.swap(index, index + 1)
                        }
                    }
                )
            }

            ElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    focusManager.clearFocus()
                    val validation = buildSequence(listName, asanaItems, sequence?.id)
                    if (validation.error != null) {
                        errorMessage = validation.error
                    } else if (validation.sequence != null) {
                        errorMessage = null
                        onSave(validation.sequence)
                    }
                }
            ) {
                Text(text = "Speichern")
            }

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorTopBar(
    isEditing: Boolean,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)?
) {
    TopAppBar(
        title = { Text(text = if (isEditing) "Sequenz bearbeiten" else "Neue Sequenz") },
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Zurück")
            }
        },
        actions = {
            if (isEditing && onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Sequenz löschen")
                }
            }
        }
    )
}

@Composable
private fun AsanaEditorCard(
    index: Int,
    state: EditableAsana,
    onUpdate: (EditableAsana) -> Unit,
    onRemove: () -> Unit,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Asana ${index + 1}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.title,
                onValueChange = { onUpdate(state.copy(title = it)) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.duration,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        onUpdate(state.copy(duration = input))
                    }
                },
                label = { Text("Dauer (Sekunden)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.description,
                onValueChange = { onUpdate(state.copy(description = it)) },
                label = { Text("Ansage / Beschreibung") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(onClick = onMoveUp, enabled = canMoveUp) {
                        Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "Nach oben")
                    }
                    IconButton(onClick = onMoveDown, enabled = canMoveDown) {
                        Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Nach unten")
                    }
                }
                TextButton(onClick = onRemove) {
                    Text(text = "Entfernen")
                }
            }
        }
    }
}

private data class EditableAsana(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val duration: String = "60",
    val description: String = ""
)

private data class ValidationResult(
    val sequence: AsanaSequence? = null,
    val error: String? = null
)

private fun buildSequence(
    listName: String,
    asanas: List<EditableAsana>,
    existingId: String?
): ValidationResult {
    val trimmedName = listName.trim()
    if (trimmedName.isEmpty()) {
        return ValidationResult(error = "Bitte gib der Sequenz einen Namen.")
    }

    if (asanas.isEmpty()) {
        return ValidationResult(error = "Füge mindestens eine Asana hinzu.")
    }

    val parsedAsanas = mutableListOf<Asana>()
    for (item in asanas) {
        val duration = item.duration.toIntOrNull()
        if (item.title.trim().isEmpty()) {
            return ValidationResult(error = "Jede Asana benötigt einen Namen.")
        }
        if (duration == null || duration <= 0) {
            return ValidationResult(error = "Die Dauer muss eine positive Zahl sein.")
        }
        parsedAsanas += Asana(
            id = item.id,
            title = item.title.trim(),
            durationSeconds = duration,
            description = item.description.trim()
        )
    }

    if (parsedAsanas.isEmpty()) {
        return ValidationResult(error = "Füge mindestens eine gültige Asana hinzu.")
    }

    val sequence = AsanaSequence(
        id = existingId ?: UUID.randomUUID().toString(),
        name = trimmedName,
        asanas = parsedAsanas
    )

    return ValidationResult(sequence = sequence)
}

private fun <T> MutableList<T>.swap(from: Int, to: Int) {
    if (from == to) return
    val temp = this[from]
    this[from] = this[to]
    this[to] = temp
}

