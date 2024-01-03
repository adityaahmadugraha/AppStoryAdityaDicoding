package com.aditya.appstoryaditya.api

import com.aditya.appstoryaditya.models.ServerResponse
import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

//    @Multipart
//    @POST("stories")
//    suspend fun inputStory(
//        @Header("Authorization") header: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//        @Part("lat") lat: RequestBody? = null,
//        @Part("lon") lon: RequestBody? = null
//    ): ServerResponse


    @POST("stories")
    suspend fun insertStori(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ServerResponse
}