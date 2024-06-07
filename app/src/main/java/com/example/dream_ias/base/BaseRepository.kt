package com.example.dream_ias.base

import com.example.dream_ias.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseRepository {
   /* suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val res = apiCall.invoke()
                if (res.isSuccessful)
                    Resource.Success(res)
                else Resource.Error(res.errorBody())
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Resource.Failure(throwable)
            }
        }
    }*/
   /* val res = apiCall.invoke()
    if (res.isSuccessful)
    Resource.Success(res)
    else Resource.Error(res.errorBody())*/

    suspend fun <T> Response<T>.intoSafe():Resource<T?>{
        return withContext(Dispatchers.IO) {
            try {
                if (isSuccessful)
                    Resource.Success(this@intoSafe.body())
                else Resource.Error(errorBody())
            } catch (throwable: Throwable) {
                Resource.Failure(throwable)
            }
        }
    }
   /* suspend fun safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val res = apiCall.invoke()
                if (res.isSuccessful)
                    Resource.Success(res)
                else Resource.Error(res.errorBody())
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Resource.Failure(throwable)
            }
        }
    }*/

}