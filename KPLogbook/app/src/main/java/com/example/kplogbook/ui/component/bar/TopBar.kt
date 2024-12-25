package com.example.kplogbook.ui.component.bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onFilterClick: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onSearchTextChange(it)
                },
                placeholder = { Text("Search...", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(onClick = {
                if (searchText.isNotEmpty()) {
                    navController.navigate("searchResults/$searchText")
                }
            }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { onFilterClick() }) {
                Icon(Icons.Default.FilterAlt, contentDescription = "Filter", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF4CAF50), // Green color
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarWithSearchAndFilterPreview() {
    val navController = rememberNavController()
    TopBar(
        onFilterClick = { /* Handle filter click */ },
        onSearchTextChange = { /* Handle search text change */ },
        navController = navController
    )
}
