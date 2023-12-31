package com.aditya.appstoryaditya.ui.main

import com.aditya.appstoryaditya.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

//    fun inputStory(
//        token: String,
//        fotoStory: MultipartBody.Part,
//        description: RequestBody,
//        lat: RequestBody,
//        lon: RequestBody?,
//        lon1: RequestBody?
//    )=  flow {
//        emit(Resource.Loading())
//        val response = apiService.inputStory(token, fotoStory, description, lat, lon)
//        emit(Resource.Success(response))
//    }.catch {
//        emit(Resource.Error(it.message ?: ""))
//    }.flowOn(Dispatchers.IO)
}