package com.aditya.appstoryaditya.repository

import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.models.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val remoteData: StoryAppRepository,
    private val localData: UserPreference
) {
    fun loginUser(request: LoginRequest) = remoteData.loginUser(request)

    fun getUser() = localData.getUser()

//    fun getDataUser(token: String) = remoteData.GetUser(token)
    suspend fun saveUser(user: User) = localData.saveUser(user)

//    suspend fun deleteUserLocal() = localData.deleteUser(user)


    fun insertStory(
        token: String,
        fotoStory: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = remoteData.inputStory(token, fotoStory, description, lat, lon)

}