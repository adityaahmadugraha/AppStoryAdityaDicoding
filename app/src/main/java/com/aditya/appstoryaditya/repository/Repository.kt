package com.aditya.appstoryaditya.repository

import com.aditya.appstoryaditya.api.ApiService
import com.aditya.appstoryaditya.models.ServerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun inputStory(
        header: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): ServerResponse {
        return apiService.inputStory(header, file, description, lat, lon)
    }
}
