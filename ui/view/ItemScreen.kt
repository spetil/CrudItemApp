package com.example.cruditemapp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // ← necessário para definir as cores
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cruditemapp.model.Item
import com.example.cruditemapp.viewmodel.ItemViewModel

@Composable
fun ItemScreen(
    modifier: Modifier = Modifier,
    viewModel: ItemViewModel = viewModel()
) {
    val items by viewModel.items

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Título") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (title.text.isNotEmpty() && description.text.isNotEmpty()) {
                    viewModel.addItem(
                        Item(title = title.text, description = description.text)
                    )
                    title = TextFieldValue("")
                    description = TextFieldValue("")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF009688), 
                contentColor = Color.White         
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Adicionar")
        }

        LazyColumn {
            items(items.size) { index ->
                val item = items[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text(text = "Título: ${item.title}")
                        Text(text = "Descrição: ${item.description}")

                        Row {
                            Button(
                                onClick = { viewModel.deleteItem(item.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE53935), 
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Delete")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    selectedItem = item
                                    showDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3949AB),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Update")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        UpdateItemDialog(
            item = selectedItem,
            onDismiss = { showDialog = false },
            onUpdate = { updateItem ->
                viewModel.updateItem(updateItem)
                showDialog = false
            }
        )
    }
}

@Composable
fun UpdateItemDialog(
    item: Item?,
    onDismiss: () -> Unit,
    onUpdate: (Item) -> Unit
) {
    if (item == null) return

    var title by remember { mutableStateOf(TextFieldValue(item.title)) }
    var description by remember { mutableStateOf(TextFieldValue(item.description)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Item") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Descrição") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdate(item.copy(title = title.text, description = description.text))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF43A047), 
                    contentColor = Color.White
                )
            ) {
                Text(text = "Salvar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF757575),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Cancelar")
            }
        }
    )
}
