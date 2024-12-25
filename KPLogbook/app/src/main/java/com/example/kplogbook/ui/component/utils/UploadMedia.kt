package com.example.kplogbook.ui.component.utils

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kplogbook.ui.theme.Primary
import com.example.kplogbook.ui.theme.White

@Composable
fun UploadMedia() {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedFileUri = result.data?.data.toString()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Media/Poster Acara",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(White, RoundedCornerShape(8.dp))
                .clickable {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "image/*"
                    }
                    launcher.launch(intent)
                },
            contentAlignment = Alignment.Center
        ) {
            if (selectedFileUri == null) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_upload),
                    contentDescription = "Upload Icon",
                    tint = Primary,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text(
                    text = "File Selected",
                    color = Primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
            }
        }
    }
}