package com.example.kplogbook.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kplogbook.ui.component.bar.BottomNavBar
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kplogbook.ViewModelFactory
import com.example.kplogbook.data.response.DataItem


@Composable
fun HomeScreen(navController: NavController,
                viewModel: HomeViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
               ) {

    val requests by viewModel.requests.collectAsState()



    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "KP Requests",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(requests) { request ->
                    RequestCard(request = request, navController = navController)
                }
            }
        }
    }
}

@Composable
fun RequestCard(request: DataItem, navController: NavController, ) {
    Card(
        onClick = {
            navController.navigate("requestDetail/${request.id}")  // Use the exact route with ID
        },
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = request.group.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                StatusChip(
                    status = request.status,
                )
            }


            Text(
                text = request.company,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))


        }
    }
}

@Composable
fun StatusChip(status: String) {
    val backgroundColor = when(status) {
        "Approved" -> Color(0xFFDFF0D8) // Light green
        "Pending" -> Color(0xFFFFF8E1) // Light yellow
        "Rejected" -> Color(0xFFF2DEDE) // Light red
        else -> MaterialTheme.colorScheme.surface
    }

    val textColor = when(status) {
        "Approved" -> Color(0xFF3C763D) // Dark green
        "Pending" -> Color(0xFF8A6D3B) // Dark yellow
        "Rejected" -> Color(0xFFA94442) // Dark red
        else -> MaterialTheme.colorScheme.onSurface
    }


    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

data class KPRequest(
    val groupName: String,
    val company: String,
    val status: String,
    val id: String
)



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}