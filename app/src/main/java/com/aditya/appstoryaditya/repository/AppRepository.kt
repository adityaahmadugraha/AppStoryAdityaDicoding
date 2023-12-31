package com.aditya.appstoryaditya.repository

import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.RegisterRequest
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.models.UserPreference
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localData: UserPreference
) {

    fun registerUser(request: RegisterRequest) = remoteDataSource.registerUser(request)

    fun loginUser(request: LoginRequest) = remoteDataSource.loginUser(request)


    fun getUser() = localData.getUser()

    suspend fun saveUser(userLocal: User) = localData.saveUser(userLocal)

    suspend fun deleteUser() = localData.deleteUser()

}