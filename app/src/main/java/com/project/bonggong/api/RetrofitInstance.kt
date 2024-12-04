package com.project.bonggong.api

import com.project.bonggong.BuildConfig
import com.project.bonggong.model.GptApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val apiKey = BuildConfig.OPENAI_API_KEY

object RetrofitInstance {

    // 로그를 위한 Interceptor 설정
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient 설정
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("OpenAI-Beta", "assistants=v2")
                .addHeader("Authorization", "Bearer ${apiKey}")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit 설정
    private val  retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API service 인스턴스 생성
    val api: GptApiService by lazy {
        retrofit.create(GptApiService::class.java)
    }
}