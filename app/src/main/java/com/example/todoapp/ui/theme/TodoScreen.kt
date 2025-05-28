package com.example.todoapp.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.viewmodel.TodoViewModel
import com.example.todoapp.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val todos by viewModel.todos.collectAsState()
    var text by remember { mutableStateOf("") }
    var isAdding by remember { mutableStateOf(false) }

    // Modern gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFFe0e7ff), // Soft blue
//                        Color(0xFFf8fafc), // Soft white
//                        Color(0xFFf0abfc)  // Soft purple-pink
//                    )
//                )
//            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            // Header
            Text(
                text = "Todo App",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F378B)
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Plan your day, stay productive!",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF625B71),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Add Task Card (glassmorphism)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(10.dp)) // smaller shadow
                    .clip(RoundedCornerShape(16.dp))         // smaller corners
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color(0xFFe0e7ff).copy(alpha = 0.5f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp) // reduced padding
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text("What needs to be done?", fontSize = 14.sp) }, // smaller placeholder text
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7C3AED),
                            unfocusedBorderColor = Color(0xFF7C3AED).copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp) // smaller input text
                    )
                    Spacer(modifier = Modifier.height(12.dp)) // less spacing
                    Button(
                        onClick = {
                            if (text.isNotBlank()) {
                                viewModel.addTodo(text)
                                text = ""
                                isAdding = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp), // reduced button height
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C3AED) // fallback color
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            modifier = Modifier.size(18.dp) // smaller icon
                        )
                        Spacer(modifier = Modifier.width(6.dp)) // reduced spacing
                        Text("Add Task", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) // smaller text
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            // Task List or Empty State
            if (todos.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF7C3AED).copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No tasks yet!\nAdd a task to get started.",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color(0xFF7C3AED).copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = todos,
                        key = { it.id }
                    ) { todo ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            TodoItem(
                                todo = todo,
                                onDelete = { viewModel.deleteTodo(todo) },
                                onToggle = { viewModel.toggleDone(it) },
                                onEdit = { t, newTitle ->
                                    viewModel.updateTodoTitle(t, newTitle)
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}