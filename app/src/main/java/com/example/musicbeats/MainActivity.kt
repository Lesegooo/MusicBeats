package com.example.musicbeats

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.Color

// Data class to represent a single packing item.
// This is not a Composable function, nor does it use private val/var at the class level.
data class PackingItem(val name: String, val category: String, val quantity: Int, val comments: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The entire UI for the first screen (and the second, managed by state)
        // is defined within this setContent block.
        setContent {
            // State to control which screen is currently displayed.
            // "main" for the first screen, "list" for the second screen.
            // Using 'remember' with 'mutableStateOf' directly within the lambda
            // avoids class-level private val/var declarations.
            val currentScreen = remember { mutableStateOf("main") }

            // State to hold the list of packing items.
            // 'mutableStateListOf' provides an observable list for Compose.
            val packingItems = remember { mutableStateListOf<PackingItem>() }

            // State to control the visibility of the "Add Item" dialog.
            val showAddItemDialog = remember { mutableStateOf(false) }

            // States for the input fields within the "Add Item" dialog.
            // These are also managed using 'remember' and 'mutableStateOf' locally.
            val itemNameInput = remember { mutableStateOf("") }
            val categoryInput = remember { mutableStateOf("") }
            val quantityInput = remember { mutableStateOf("") }
            val commentsInput = remember { mutableStateOf("") }

            // Get the current context to display Toast messages and finish the activity.
            val context = LocalContext.current

            // Apply Material Design theme to the app.
            MaterialTheme {
                // A surface container using the 'background' color from the theme.
                Surface(
                    modifier = Modifier.fillMaxSize(), // Fills the entire screen.
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Conditional rendering based on the 'currentScreen' state.
                    when (currentScreen.value) {
                        "main" -> {
                            // --- Screen One UI ---
                            Column(
                                modifier = Modifier
                                    .fillMaxSize() // Fills the available space.
                                    .padding(16.dp), // Adds padding around the column.
                                horizontalAlignment = Alignment.CenterHorizontally, // Centers content horizontally.
                                verticalArrangement = Arrangement.Center // Centers content vertically.
                            ) {
                                // Button to show the "Add to Packing List" dialog.
                                Button(
                                    onClick = { showAddItemDialog.value = true },
                                    modifier = Modifier
                                        .fillMaxWidth() // Makes the button fill the width.
                                        .padding(vertical = 8.dp) // Vertical padding for spacing.
                                ) {
                                    Text("Add to Playlist")
                                }

                                Spacer(Modifier.height(16.dp)) // Vertical spacing.

                                // Button to navigate to the second screen.
                                Button(
                                    onClick = { currentScreen.value = "list" },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text("Go to Second Screen")
                                }

                                Spacer(Modifier.height(16.dp)) // Vertical spacing.

                                // Button to exit the application.
                                Button(
                                    onClick = { (context as Activity).finish() }, // Finishes the current activity.
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text("Exit App")
                                }
                            }

                            // --- "Add Item" Dialog UI ---
                            // This dialog is shown when 'showAddItemDialog' is true.
                            if (showAddItemDialog.value) {
                                Dialog(onDismissRequest = { showAddItemDialog.value = false }) {
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(), // Dialog surface fills width.
                                        shape = MaterialTheme.shapes.medium, // Rounded corners for the dialog.
                                        color = MaterialTheme.colorScheme.surface // Background color for the dialog.
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp), // Padding inside the dialog.
                                            horizontalAlignment = Alignment.CenterHorizontally // Centers content.
                                        ) {
                                            Text(
                                                "Add to playlist",
                                                style = MaterialTheme.typography.headlineSmall // Larger text style.
                                            )
                                            Spacer(Modifier.height(16.dp))

                                            // Input field for Item Name.
                                            OutlinedTextField(
                                                value = itemNameInput.value,
                                                onValueChange = { itemNameInput.value = it },
                                                label = { Text("Song Title") },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            )
                                            // Input field for Category.
                                            OutlinedTextField(
                                                value = categoryInput.value,
                                                onValueChange = { categoryInput.value = it },
                                                label = { Text("Artist Name") },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            )
                                            // Input field for Quantity.
                                            OutlinedTextField(
                                                value = quantityInput.value,
                                                onValueChange = { quantityInput.value = it },
                                                label = { Text("Rating") },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            )
                                            // Input field for Comments.
                                            OutlinedTextField(
                                                value = commentsInput.value,
                                                onValueChange = { commentsInput.value = it },
                                                label = { Text("Comments") },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            )

                                            Spacer(Modifier.height(16.dp))

                                            // Row for Cancel and Add Item buttons.
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceAround // Distributes space evenly.
                                            ) {
                                                // Cancel button for the dialog.
                                                Button(onClick = {
                                                    showAddItemDialog.value =
                                                        false // Dismiss the dialog.
                                                    // Clear input fields.
                                                    itemNameInput.value = ""
                                                    categoryInput.value = ""
                                                    quantityInput.value = ""
                                                    commentsInput.value = ""
                                                }) {
                                                    Text("Cancel")
                                                }
                                                // Add Item button for the dialog.
                                                Button(onClick = {
                                                    val name = itemNameInput.value.trim()
                                                    val category = categoryInput.value.trim()
                                                    val quantityStr = quantityInput.value.trim()
                                                    val comments = commentsInput.value.trim()

                                                    // --- Error Handling for Input ---
                                                    if (name.isEmpty() || category.isEmpty() || quantityStr.isEmpty()) {
                                                        Toast.makeText(
                                                            context,
                                                            "Please fill in all required fields (Name, Category, Quantity).",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        return@Button // Exit the onClick lambda.
                                                    }

                                                    val quantity = quantityStr.toIntOrNull()
                                                    if (quantity == null || quantity <= 0) {
                                                        Toast.makeText(
                                                            context,
                                                            "Rating must be a positive number.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        return@Button // Exit the onClick lambda.
                                                    }

                                                    // Add the new item to the packing list.
                                                    packingItems.add(
                                                        PackingItem(
                                                            name,
                                                            category,
                                                            quantity,
                                                            comments
                                                        )
                                                    )
                                                    showAddItemDialog.value =
                                                        false // Dismiss the dialog.
                                                    // Clear input fields after adding.
                                                    itemNameInput.value = ""
                                                    categoryInput.value = ""
                                                    quantityInput.value = ""
                                                    commentsInput.value = ""
                                                    Toast.makeText(
                                                        context,
                                                        "Song added!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }) {
                                                    Text("Add Song")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        "list" -> {
                            // --- Screen Two UI ---
                            // State to toggle between displaying all items and items with quantity >= 2.
                            val showAllItems = remember { mutableStateOf(true) }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Button to display the full packing list.
                                    Button(onClick = { showAllItems.value = true }) {
                                        Text("Display Playlist")
                                    }
                                    // Button to display items with quantity 2 or more.
                                    Button(onClick = { showAllItems.value = false }) {
                                        Text("songs >= 2 Playlist")
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                // Display area for the packing list items.
                                if (packingItems.isEmpty()) {
                                    Text(
                                        "No songs in the playlist yet.",
                                        modifier = Modifier.padding(16.dp),
                                        color = Color.Gray // Gray text for empty state.
                                    )
                                } else {
                                    // LazyColumn is used for efficient display of a scrollable list.
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f) // Allows the list to take available vertical space.
                                    ) {
                                        // Determine which items to display based on 'showAllItems' state.
                                        val itemsToDisplay = if (showAllItems.value) {
                                            packingItems
                                        } else {
                                            packingItems.filter { it.quantity >= 2 }
                                        }
                                        items(itemsToDisplay) { item ->
                                            // Card for each packing item to give it a distinct visual appearance.
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                elevation = CardDefaults.cardElevation(
                                                    defaultElevation = 2.dp
                                                ) // Adds a subtle shadow.
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Text(
                                                        "Item: ${item.name}",
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    Text(
                                                        "Category: ${item.category}",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    Text(
                                                        "Quantity: ${item.quantity}",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    if (item.comments.isNotEmpty()) {
                                                        Text(
                                                            "Comments: ${item.comments}",
                                                            style = MaterialTheme.typography.bodySmall
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                // Button to return to the main screen.
                                Button(
                                    onClick = { currentScreen.value = "main" },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Return to Main Screen")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
