package com.example.todoapp.ui.theme

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(
    todo: Todo,
    onDelete: (Todo) -> Unit,
    onToggle: (Todo) -> Unit,
    onEdit: (Todo, String) -> Unit
) {
    var isDeleting by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(todo.isDone) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(todo.title) }

    LaunchedEffect(todo.isDone) {
        isChecked = todo.isDone
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Task") },
            text = {
                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
                    label = { Text("Task Title") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (editText.isNotBlank()) {
                        onEdit(todo, editText)
                        showEditDialog = false
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditDialog = false }) { Text("Cancel") }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        onToggle(todo.copy(isDone = it))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline
                    )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alpha(if (isChecked) 0.6f else 1f),
                    textDecoration = if (isChecked) TextDecoration.LineThrough else null
                )
            }
            
            IconButton(
                onClick = { showEditDialog = true },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(
                onClick = {
                    isDeleting = true
                    onDelete(todo)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}