package com.example.froumapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class RemoteDataSource @Inject constructor(){
    companion object {
        private const val BASE_URL = "http://10.0.2.2:5000/"
    }

    fun <Api> buildApi(
            api: Class<Api>,
            authToken: String? = null,
            userId: String? = null
    ): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor {chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Authorization", "Bearer $authToken")
                    it.addHeader("UserId", "$userId")
                }.build())
                }.also { client ->
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

}