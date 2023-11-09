package com.example.froumapp.di

import com.example.froumapp.data.network.AuthApi
import com.example.froumapp.data.network.RemoteDataSource
import com.example.froumapp.data.network.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource
    ): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java, null, null)
    }

    @Provides
    fun provideUserApi(
        remoteDataSource: RemoteDataSource
    ): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java, null, null)
    }
}