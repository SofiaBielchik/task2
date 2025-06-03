package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab2.ui.theme.Lab2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var showResultFragment by remember { mutableStateOf(false) }
    var selectedAuthor by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            confirmButton = {
                TextButton(onClick = { showAlert = false }) {
                    Text("OK")
                }
            },
            title = { Text("Увага") },
            text = { Text("Будь ласка, введіть всі дані перед натисканням кнопки 'ОК'.") }
        )
    }

    if (!showResultFragment) {
        // Перший фрагмент — форма введення
        InputFragment(
            selectedAuthor = selectedAuthor,
            onAuthorSelected = { selectedAuthor = it },
            selectedYear = selectedYear,
            onYearSelected = { selectedYear = it },
            onOkClick = {
                if (selectedAuthor.isEmpty() || selectedYear.isEmpty()) {
                    showAlert = true
                    resultText = ""
                } else {
                    resultText = "Автор: $selectedAuthor\nРік видання: $selectedYear"
                    showResultFragment = true
                }
            }
        )
    } else {
        // Другий фрагмент — відображення результату
        ResultFragment(
            resultText = resultText,
            onCancelClick = {
                // Очистити дані і повернутись до першого фрагменту
                selectedAuthor = ""
                selectedYear = ""
                resultText = ""
                showResultFragment = false
            }
        )
    }
}

@Composable
fun InputFragment(
    selectedAuthor: String,
    onAuthorSelected: (String) -> Unit,
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    onOkClick: () -> Unit
) {
    val authors = listOf(
        "Тарас Шевченко",
        "Ліна Костенко",
        "Леся Українка",
        "Іван Франко"
    )
    val years = listOf(
        "1840",
        "1960",
        "1910",
        "1890"
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Введення інформації про книгу", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Оберіть автора", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
        ) {
            OutlinedTextField(
                value = selectedAuthor,
                onValueChange = {},
                readOnly = true,
                label = { Text("Автор") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                authors.forEach { author ->
                    DropdownMenuItem(
                        text = { Text(author) },
                        onClick = {
                            onAuthorSelected(author)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Оберіть рік видання", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        years.forEach { year ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (year == selectedYear),
                        onClick = { onYearSelected(year) },
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (year == selectedYear),
                    onClick = { onYearSelected(year) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = year, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onOkClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ОК")
        }
    }
}

@Composable
fun ResultFragment(
    resultText: String,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Результат", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text(resultText, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Lab2Theme {
        MainScreen()
    }
}
