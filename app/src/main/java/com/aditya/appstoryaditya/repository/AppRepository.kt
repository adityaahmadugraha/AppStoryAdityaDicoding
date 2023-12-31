package com.aditya.appstoryaditya.repository

import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.models.UserPreference
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreference: UserPreference
) {

    fun loginUser(request: LoginRequest) = remoteDataSource.loginUser(request)

    fun getUser() = userPreference.getUser()

    suspend fun saveUser(userLocal: User) = userPreference.saveUser(userLocal)

    suspend fun deleteUser() = userPreference.deleteUser()

}