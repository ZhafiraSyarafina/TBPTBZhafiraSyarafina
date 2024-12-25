package com.example.kplogbook.ui.component.bar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kplogbook.R
import com.example.kplogbook.ui.theme.Background
import com.example.kplogbook.ui.theme.Primary

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Log.d("BottomNavBar", "Rendering BottomNavBar with NavController: $navController")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(Background),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.home),
                contentDescription = "Home",
                tint = Primary,
            )
        }
        IconButton(
            onClick = { navController.navigate("info") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.laporan),
                contentDescription = "Add_Event",
                tint = Primary
            )
        }
        IconButton(
            onClick = { navController.navigate("arsip") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.pengajuan),
                contentDescription = "Add_Event",
                tint = Primary
            )
        }
        IconButton(
            onClick = { navController.navigate("reply") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.logbook),
                contentDescription = "Reply",
                tint = Primary
            )
        }
        IconButton(
            onClick = {
                try {
                    navController.navigate("profile")
                } catch (e: Exception) {
                    Log.e("BottomNavBar", "Error navigating to profile", e)
                    throw e
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.profile),
                contentDescription = "Profile",
                tint = Primary
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController()
    BottomNavBar(navController = navController)
}
