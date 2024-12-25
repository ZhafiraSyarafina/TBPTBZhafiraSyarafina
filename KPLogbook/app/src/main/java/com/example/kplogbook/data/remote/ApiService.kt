package com.example.kplogbook.data.remote


import com.example.kplogbook.data.request.ChangePasswordRequest
import com.example.kplogbook.data.request.CreateGroupRequest
import com.example.kplogbook.data.request.LoginRequest
import com.example.kplogbook.data.request.RegisterRequest
import com.example.kplogbook.data.request.RequestEdit
import com.example.kplogbook.data.response.AddRequestResponse
import com.example.kplogbook.data.response.CreateGroupResponse
import com.example.kplogbook.data.response.GroupResponse
import com.example.kplogbook.data.response.LoginResponse
import com.example.kplogbook.data.response.MembersResponse
import com.example.kplogbook.data.response.MyGroupResponse
import com.example.kplogbook.data.response.RegisterResponse
import com.example.kplogbook.data.response.RequestResponse
import com.example.kplogbook.data.response.RequestsResponse
import com.example.kplogbook.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse


    @GET("/members")
    suspend fun getMembers(
    ): MembersResponse

    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): UserResponse

    @POST("/update-phone")
    suspend fun updatePhone(
        @Header("Authorization") token: String,
        @Body phone: String
    ): Response<Void>

    @POST("/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<Void>
    @GET("/requests")
    suspend fun getRequests(
        @Header("Authorization") token: String
    ): RequestsResponse

    @GET("/myRequest")
    suspend fun getRequest(
        @Header("Authorization") token: String
    ): RequestResponse

    @POST("/groups")
    suspend fun createGroup(
        @Body request: CreateGroupRequest
    ): CreateGroupResponse


    @GET("/group")
    suspend fun getMyGroup(
        @Header("Authorization") token: String
    ): MyGroupResponse

    @Multipart
    @POST("/request")
    suspend fun registerKP(
        @Header("Authorization") token: String,
        @Part("groupId") groupId: RequestBody,
        @Part("company") company: RequestBody,
        @Part("startDate") startDate: RequestBody,
        @Part("endDate") endDate: RequestBody,
        @Part proposal: MultipartBody.Part
    ): Response<Void>

    @PUT("myRequest")
    suspend fun updateRequest(
        @Header("Authorization") token: String,
        @Body requestEdit: RequestEdit
    ): Response<Void>

}



