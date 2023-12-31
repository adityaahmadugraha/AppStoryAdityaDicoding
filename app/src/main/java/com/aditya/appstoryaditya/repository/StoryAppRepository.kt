package com.aditya.appstoryaditya.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aditya.appstoryaditya.api.ApiService
import com.aditya.appstoryaditya.models.ServerResponse
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.database.StoryDatabase
import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.RegisterRequest
import com.aditya.appstoryaditya.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class StoryAppRepository @Inject constructor(
    private val apiService: ApiService,
    private val db: StoryDatabase
) {
    fun registerUser(registerRequest: RegisterRequest) = flow<Resource<ServerResponse>> {
        emit(Resource.loading())
        val response = apiService.registerUser(registerRequest)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.error(it.message))
        }
    }.catch {
        Log.d(TAG, "registerUser: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)


    fun loginUser(loginRequest: LoginRequest) = flow<Resource<ServerResponse>> {
        emit(Resource.loading())
        val response = apiService.loginUser(loginRequest)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.Error(it.message))
        }
    }.catch {
        Log.d(TAG, "loginUser: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    @ExperimentalPagingApi
    fun getAllStories(
        token: String
    ): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(apiService, db, token),
            pagingSourceFactory = {
                db.storyDao().getAllStory()
            }
        ).flow
    }

    fun getStoryWithLocation(
        token: String,
        location: Int?
    ) = flow<Resource<ServerResponse>> {
        emit(Resource.loading())
        val response = apiService.getAllStories(token, location = location)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.error(it.message))
        }
    }.catch {
        Log.d(TAG, "getAllStories: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)



    fun inputStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = flow<Resource<ServerResponse>> {
        emit(Resource.loading())
        val response = apiService.inputStory(token, file, description, lat, lon)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.error(it.message))
        }
    }.catch {
        Log.d(TAG, "getAllStories: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

}
