package com.example.kplogbook.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kplogbook.data.UserRepository
import com.example.kplogbook.data.response.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    // StateFlow untuk menangani daftar request
    private val _requests = MutableStateFlow<List<DataItem>>(emptyList())
    val requests: StateFlow<List<DataItem>> = _requests.asStateFlow()

    // StateFlow untuk menangani loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()



    init {
        getRequests()
    }


    // Fungsi untuk mendapatkan data request dari API
    private fun getRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getRequests()
                // Mengubah response API menjadi daftar KPRequest yang sesuai dengan data yang diinginkan
                _requests.value = response.data?.filterNotNull() ?: emptyList()
                Log.d("HomeViewModel", "Requests fetched: ${_requests.value}")
            } catch (e: Exception) {
                // Handle error (optional)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
