package com.example.gfit.data.network.service

import android.util.Log
import com.example.gfit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
    private const val API_KEY = "AIzaSyDXKFc7cnlebBD7QFlJT9I2CMvNCHXR7TI"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("RetrofitClient", "🌐 Requête URL: ${request.url}")

            val response = chain.proceed(request)
            Log.d("RetrofitClient", """
                📥 Réponse reçue:
                Code: ${response.code}
                Message: ${response.message}
                Corps: ${response.peekBody(Long.MAX_VALUE).string()}
            """.trimIndent())

            response
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val workoutApiService: WorkoutApiService by lazy {
        retrofit.create(WorkoutApiService::class.java)
    }
    fun getApiKey(): String = API_KEY
}