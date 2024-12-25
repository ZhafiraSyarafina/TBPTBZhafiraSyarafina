package com.example.kplogbook.ui.pendaftaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kplogbook.data.UserRepository
import kotlinx.coroutines.launch

class PendaftaranViewModel (private val repository: UserRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}