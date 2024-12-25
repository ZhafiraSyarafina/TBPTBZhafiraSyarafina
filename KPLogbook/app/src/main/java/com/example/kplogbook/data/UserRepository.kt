package com.example.kplogbook.data

import android.util.Log
import com.example.kplogbook.data.pref.UserModel
import com.example.kplogbook.data.pref.UserPreference
import com.example.kplogbook.data.remote.ApiService
import com.example.kplogbook.data.request.ChangePasswordRequest
import com.example.kplogbook.data.request.CreateGroupRequest
import com.example.kplogbook.data.request.LoginRequest
import com.example.kplogbook.data.request.RegisterRequest
import com.example.kplogbook.data.request.RequestEdit
import com.example.kplogbook.data.response.CreateGroupResponse


import com.example.kplogbook.data.response.LoginResponse
import com.example.kplogbook.data.response.MembersResponse
import com.example.kplogbook.data.response.MyGroupResponse
import com.example.kplogbook.data.response.RegisterResponse
import com.example.kplogbook.data.response.RequestResponse
import com.example.kplogbook.data.response.RequestsResponse
import com.example.kplogbook.data.response.UserResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File


class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(email, password))
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(RegisterRequest(name, email, password))
    }

    private fun getToken(): String {
        val user = runBlocking { userPreference.getSession().first() }
        Log.d("UserRepository", "Token: ${user.token}")
        return runBlocking { userPreference.getSession().first().token }
    }

    suspend fun getUser(): UserResponse {
        val token = getToken()
        try {
            val dataUser = apiService.getUser("Bearer $token")
            Log.d("UserRepository", "User data: $dataUser")
            return apiService.getUser("Bearer $token")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                logout()
                throw e
            } else {
                throw e
            }
        }
    }



    suspend fun getAvailableMembers(): MembersResponse {
        return apiService.getMembers()
    }

    suspend fun createGroup(request: CreateGroupRequest): CreateGroupResponse {
        return apiService.createGroup(request)
    }

    suspend fun updateRequest(request: RequestEdit): Response<Void> {
        val token = getToken()
        return apiService.updateRequest("Bearer $token", request)
    }

    suspend fun getMyGroup(): MyGroupResponse {
        val token = getToken()
        Log.d("UserRepository", "Token: $token")
        return apiService.getMyGroup("Bearer $token")
    }




    suspend fun registerKP(
        groupId: String,
        company: String,
        startDate: String,
        endDate: String,
        proposal: MultipartBody.Part
    ): Response<Void> {
        val token = getToken()

        // Convert string parameters to RequestBody
        val groupIdBody = groupId.toRequestBody("text/plain".toMediaType())
        val companyBody = company.toRequestBody("text/plain".toMediaType())
        val startDateBody = startDate.toRequestBody("text/plain".toMediaType())
        val endDateBody = endDate.toRequestBody("text/plain".toMediaType())

        return apiService.registerKP(
            token = "Bearer $token",
            groupId = groupIdBody,
            company = companyBody,
            startDate = startDateBody,
            endDate = endDateBody,
            proposal = proposal
        )
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun updatePhone(phone: String): Response<Void> {
        val token = getToken()
        return apiService.updatePhone(token, phone)
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Response<Void> {
        val token = getToken()
        return apiService.changePassword(
            token = "Bearer $token",
            request = ChangePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }

    suspend fun getRequests(): RequestsResponse {
        val token = getToken()

        return apiService.getRequests(token)
    }

    suspend fun getRequestById(): RequestResponse {
        val token = getToken()
        return apiService.getRequest( "Bearer $token")
    }





    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
