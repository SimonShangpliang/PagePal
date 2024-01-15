package com.example.imgselect.DictionaryNetwork

import com.example.imgselect.DictionaryNetwork.DictionaryApi.retrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path


private const val URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"


private val json = Json {
    ignoreUnknownKeys = true
}




private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(URL)
    .build()



interface DictionaryApiService {
    @GET("{word}")
    suspend fun getMeaning(@Path("word") word: String): List<WordData>
    {
        try {
            return retrofitService.getMeaning(word)
        } catch (e: HttpException) {
            // Handle 404 error (or any other HTTP error code) here
            // Log or show an error message indicating the resource was not found
            e.printStackTrace()
            throw e // Optionally, rethrow the exception for higher-level handling
        }
    }
}

object DictionaryApi {
    val retrofitService : DictionaryApiService by lazy {
        retrofit.create(DictionaryApiService::class.java)
    }
}