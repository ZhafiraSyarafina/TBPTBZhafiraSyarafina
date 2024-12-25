package com.example.kplogbook.ui.registration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kplogbook.data.UserRepository
import com.example.kplogbook.data.Result
import com.example.kplogbook.data.response.DataGroup
import com.example.kplogbook.data.response.KPRequest
import com.example.kplogbook.data.response.MyGroupData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDate

class KPRegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {
    val myGroup = mutableStateOf<MyGroupData?>(null)
    val companyName = mutableStateOf("")
    val startDate = mutableStateOf("")
    val endDate = mutableStateOf("")
    val proposalFile = mutableStateOf<File?>(null)

    init {
        fetchMyGroup()
    }

    private fun fetchMyGroup() {
        viewModelScope.launch {
            try {
                val group = userRepository.getMyGroup() // Endpoint `/group` to fetch the user's group
                myGroup.value = group.data
            } catch (e: Exception) {
                // Handle error, e.g., log or show message
            }
        }
    }

    fun updateProposal(file: File) {
        proposalFile.value = file
    }

    fun submitKPRequest(groupId: String,companyName: String,startDate: String,endDate: String, proposal : MultipartBody.Part,onSuccess: () -> Unit,
                        onError: (Throwable) -> Unit    ) {
        viewModelScope.launch {
            try {
                userRepository.registerKP(groupId,companyName,startDate,endDate,proposal)
                onSuccess()



            } catch (e: Exception) {
                // Tangani exception
            }
        }
    }

}
