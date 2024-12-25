package com.example.kplogbook.ui.requestDetail

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kplogbook.data.UserRepository
import com.example.kplogbook.data.request.RequestEdit
import com.example.kplogbook.data.response.DataDetailRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class RequestDetailViewModel(
    private val repository: UserRepository,
    private val context: Context
) : ViewModel() {

    private val _request = MutableStateFlow<DataDetailRequest?>(null)
    val request: StateFlow<DataDetailRequest?> = _request.asStateFlow()

    private val _downloadStatus = MutableStateFlow<DownloadStatus>(DownloadStatus.Idle)
    val downloadStatus: StateFlow<DownloadStatus> = _downloadStatus.asStateFlow()


    private fun monitorDownload(downloadId: Long) {
        viewModelScope.launch {
            var downloading = true
            while (downloading) {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
                    .query(query)

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> downloading = false
                        DownloadManager.STATUS_FAILED -> {
                            downloading = false
                            _downloadStatus.value = DownloadStatus.Error("Download failed")
                        }
                    }
                }
                cursor.close()
                delay(1000) // Check every second
            }
        }
    }

    fun updateRequest(startDate: String, endDate: String, company: String) {
        viewModelScope.launch {
            try {
                repository.updateRequest(RequestEdit(startDate, endDate, company))
            } catch (e: Exception) {
            }
        }

    }

    fun getRequest() {
        viewModelScope.launch {
            try {
                val response = repository.getRequestById()
                _request.value = response.data
                Log.d("RequestDetailViewModel", "Request: ${response.data}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}

sealed class DownloadStatus {
    object Idle : DownloadStatus()
    object Loading : DownloadStatus()
    object Success : DownloadStatus()
    data class Error(val message: String) : DownloadStatus()
}
