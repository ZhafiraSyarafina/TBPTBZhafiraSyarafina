package com.example.kplogbook.ui.component.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EventItem(
    navController: NavController,
    eventId: String,  // Add this parameter
    eventName: String,
    eventDescription: String,
    eventImage: Painter
) {
    Card(
        onClick = {
            navController.navigate("eventDetail/$eventId")  // Use the exact route with ID
        },
        modifier = Modifier
            .padding(8.dp)
            .size(200.dp)
            .clickable { navController.navigate("eventDetails") }, // Handle navigation on click
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Image(
                painter = eventImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = eventName,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = eventDescription,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

