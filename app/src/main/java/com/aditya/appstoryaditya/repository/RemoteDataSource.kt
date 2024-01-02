package com.aditya.appstoryaditya.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aditya.appstoryaditya.api.ApiService
import com.aditya.appstoryaditya.database.StoryDatabase
import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.RegisterRequest
import com.aditya.appstoryaditya.models.ServerResponse
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val db: StoryDatabase,
    private val remoteDataSource : Repository
) {

    fun registerUser(registerRequest: RegisterRequest) = flow {
        emit(Resource.Loading())
        val response = apiService.registerUser(registerRequest)
        emit(Resource.Success(response))
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)


    fun loginUser(loginRequest: LoginRequest) = flow {
        emit(Resource.Loading())
        val response = apiService.loginUser(loginRequest)
        emit(Resource.Success(response))
    }.catch {
        emit(Resource.Error(it.message ?: ""))
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
        emit(Resource.Loading())
        val response = apiService.getStories(token, location = location)
        response.let {
            if (!it.error) emit(Resource.Success(it))
            else emit(Resource.Error(it.message))
        }
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)



    fun inputStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<Resource<ServerResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = remoteDataSource.inputStory(token, file, description, lat, lon)
            if (!response.error) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

}
