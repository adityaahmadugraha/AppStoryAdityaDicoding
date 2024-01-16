package com.aditya.appstoryaditya.api

import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.RegisterRequest
import com.aditya.appstoryaditya.models.ServerResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): ServerResponse

    @POST("login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): ServerResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") header: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): ServerResponse

    @POST("stories")
    suspend fun insertStori(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ServerResponse
}