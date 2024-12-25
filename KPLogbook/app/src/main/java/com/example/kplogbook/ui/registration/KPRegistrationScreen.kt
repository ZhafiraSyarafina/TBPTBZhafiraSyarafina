package com.example.kplogbook.ui.registration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kplogbook.ViewModelFactory
import com.example.kplogbook.data.UserRepository
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KPRegistrationScreen(
    navController: NavController,
    viewModel: KPRegistrationViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
) {
    val group by viewModel.myGroup
    val context = LocalContext.current

    var selectedFile by remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFile = uri
    }

    // States for DatePicker
    val calendar = Calendar.getInstance()
    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }

    val startDatePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            startDate.value = "$day-${month + 1}-$year"
            viewModel.startDate.value = startDate.value
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            endDate.value = "$day-${month + 1}-$year"
            viewModel.endDate.value = endDate.value
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pendaftaran KP", style = MaterialTheme.typography.headlineMedium)

        if (group != null) {
            // Display Group Details
            Text("Kelompok Anda:", style = MaterialTheme.typography.titleMedium)
            Text("Nama Kelompok: ${group!!.name}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Anggota Kelompok:", style = MaterialTheme.typography.titleSmall)
            group!!.members.forEach { member ->
                Text("- ${member.name} (${member.nim})", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Fields
            OutlinedTextField(
                value = viewModel.companyName.value,
                onValueChange = { viewModel.companyName.value = it },
                label = { Text("Instansi Tujuan") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = startDate.value,
                onValueChange = { startDate.value = it },
                label = { Text("Tanggal Mulai") },
                modifier = Modifier.fillMaxWidth().clickable { startDatePicker.show() },
                readOnly = true
            )

            OutlinedTextField(
                value = endDate.value,
                onValueChange = { endDate.value = it },
                label = { Text("Tanggal Selesai") },
                modifier = Modifier.fillMaxWidth().clickable { endDatePicker.show() },
                readOnly = true
            )

            Button(onClick = {launcher.launch("application/pdf") },
                modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Upload, contentDescription = "Upload Proposal")
                Text("Unggah Proposal")
            }

            Spacer(modifier = Modifier.height(16.dp))


            fun Uri.getFilePathFromUri(context: Context): File? {
                return try {
                    val inputStream = context.contentResolver.openInputStream(this)
                    val tempFile = File.createTempFile("upload", ".pdf", context.cacheDir)
                    tempFile.deleteOnExit()
                    inputStream?.use { input ->
                        tempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    tempFile
                } catch (e: Exception) {
                    Log.e("FileUpload", "Error converting URI to File", e)
                    null
                }
            }


            // Submit Button
            Button(
                onClick = {

                    val posterPart = selectedFile?.let { uri ->
                        val file = uri.getFilePathFromUri(context)
                        val requestBody = file?.asRequestBody("application/pdf".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("proposal", file?.name, requestBody!!)
                    }


                    if (selectedFile != null) {
                        viewModel.submitKPRequest( viewModel.myGroup.value?.id.toString(), viewModel.companyName.value, startDate.value, endDate.value,
                            posterPart!!,
                            onSuccess = {
                                navController.navigate("request") {
                                    popUpTo("request") { inclusive = true }
                                }
                            },
                            onError = { e ->
                                Log.e("Add request", "Error adding request", e)
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Submit")
            }
        } else {
            // Show loading or error message
            Text("Memuat data kelompok...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}