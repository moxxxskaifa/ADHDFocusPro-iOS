package com.adhdFocus.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adhdFocus.app.platform.HapticFeedback
import kotlinx.coroutines.delay

data class Task(val name: String, val done: Boolean = false)

@Composable
fun MainScreen() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTask by remember { mutableStateOf("") }
    var timerSec by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var totalPomodoros by remember { mutableIntStateOf(0) }
    val haptic = remember { HapticFeedback() }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timerSec > 0) { delay(1000L); timerSec-- }
            totalPomodoros++; haptic.medium(); isRunning = false; timerSec = 25 * 60
        }
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("ADHD Focus", fontWeight = FontWeight.Bold) }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            // Timer
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("%02d:%02d".format(timerSec / 60, timerSec % 60), fontSize = 48.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { isRunning = !isRunning },
                               colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary),
                               shape = RoundedCornerShape(12.dp)) { Text(if (isRunning) "Pause" else "Start", fontWeight = FontWeight.Bold) }
                        OutlinedButton(onClick = { isRunning = false; timerSec = 25 * 60 }, shape = RoundedCornerShape(12.dp)) { Text("Reset") }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("{totalPomodoros} pomodoros today", fontSize = 12.sp, color = MaterialTheme.colorScheme.MaterialTheme.colorScheme.oncolorScheme.surfaceVariant)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Tasks", fontWeight = FontWeight.SemiBold); Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = newTask, onValueChange = { newTask = it }, modifier = Modifier.weight(1f),
                    placeholder = { Text("Add task...") }, singleLine = true, shape = RoundedCornerShape(12.dp))
                IconButton(onClick = { if (newTask.isNotBlank()) { tasks = tasks + Task(newTask); newTask = "" } }) { Icon(Icons.Default.Add, "Add") }
            }
            Spacer(Modifier.height(12.dp))
            if (tasks.isEmpty()) { Text("No tasks yet. Break down your work!", color = MaterialTheme.colorScheme.MaterialTheme.colorScheme.oncolorScheme.surfaceVariant) }
            else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(tasks) { task ->
                        Card(shape = RoundedCornerShape(12.dp)) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = task.done, onCheckedChange = {})
                                Text(task.name, modifier = Modifier.weight(1f), fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
