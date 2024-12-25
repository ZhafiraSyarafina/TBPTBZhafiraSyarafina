package com.example.kplogbook

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kplogbook.data.UserRepository
import com.example.kplogbook.di.Injection
import com.example.kplogbook.ui.group.CreateGroupViewModel
import com.example.kplogbook.ui.home.HomeViewModel
import com.example.kplogbook.ui.login.LoginViewModel
import com.example.kplogbook.ui.pendaftaran.PendaftaranViewModel
import com.example.kplogbook.ui.registration.KPRegistrationViewModel
import com.example.kplogbook.ui.requestDetail.RequestDetailViewModel

class ViewModelFactory(private val repository: UserRepository,
                       private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RequestDetailViewModel::class.java) -> {
                RequestDetailViewModel(repository, context) as T
            }
            modelClass.isAssignableFrom(KPRegistrationViewModel::class.java) -> {
                KPRegistrationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CreateGroupViewModel::class.java) -> {
                CreateGroupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PendaftaranViewModel::class.java) -> {
                PendaftaranViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    context.applicationContext // Gunakan applicationContext untuk menghindari memory leak
                ).also { INSTANCE = it }
            }
        }
    }
}