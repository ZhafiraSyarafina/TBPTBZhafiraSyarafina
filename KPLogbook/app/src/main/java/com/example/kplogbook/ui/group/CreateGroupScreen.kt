package com.example.kplogbook.ui.group

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kplogbook.ViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    navController: NavController,
    viewModel: CreateGroupViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Filter members based on search query
    val filteredMembers = state.availableMembers.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Buat Kelompok KP",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Logged in user card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Ketua Kelompok",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = state.currentUser?.name ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = state.currentUser?.nim ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Group Name Input
        OutlinedTextField(
            value = state.groupName,
            onValueChange = { viewModel.updateGroupName(it) },
            label = { Text("Nama Kelompok") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Member Selection Section
        Text(
            text = "Pilih Anggota Kelompok",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        // Searchable Dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    isDropdownExpanded = true
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Cari Mahasiswa") },
                leadingIcon = {
                    Icon(Icons.Default.Search, "search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, "clear")
                        }
                    }
                }
            )

            DropdownMenu(
                expanded = isDropdownExpanded && filteredMembers.isNotEmpty(),
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(max = 300.dp)
            ) {
                filteredMembers.forEach { member ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(member.name)
                                Text(
                                    member.nim,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            viewModel.addMember(member.id)
                            searchQuery = ""
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Selected Members
        Text(
            text = "Anggota Terpilih",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                state.selectedMembers.forEach { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(member.name)
                            Text(
                                member.nim,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { viewModel.removeMember(member.id) }
                        ) {
                            Icon(Icons.Default.Clear, "remove member")
                        }
                    }
                }
            }
        }

        // Create Button
        Button(
            onClick = {
                viewModel.createGroup { success ->
                    if (success) {
                        Toast.makeText(context, "Kelompok berhasil dibuat", Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    } else {
                        Toast.makeText(context, "Gagal membuat kelompok", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = state.groupName.isNotBlank() && state.selectedMemberIds.isNotEmpty()
        ) {
            Text("Buat Kelompok")
        }
    }
}