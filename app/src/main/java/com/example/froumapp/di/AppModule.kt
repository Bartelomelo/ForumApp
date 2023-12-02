package com.example.froumapp.di

import com.example.froumapp.data.network.AuthApi
import com.example.froumapp.data.network.CategoryApi
import com.example.froumapp.data.network.ForumApi
import com.example.froumapp.data.network.NotificationsApi
import com.example.froumapp.data.network.PostApi
import com.example.froumapp.data.network.ThreadApi
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

    @Provides
    fun provideHomeApi(
        remoteDataSource:  RemoteDataSource
    ): ThreadApi {
        return remoteDataSource.buildApi(ThreadApi::class.java, null, null)
    }

    @Provides
    fun providePostApi(
        remoteDataSource: RemoteDataSource
    ): PostApi {
        return remoteDataSource.buildApi(PostApi::class.java, null, null)
    }

    @Provides
    fun provideCategoryApi(
        remoteDataSource: RemoteDataSource
    ): CategoryApi {
        return remoteDataSource.buildApi(CategoryApi::class.java, null, null)
    }

    @Provides
    fun provideForumApi(
        remoteDataSource: RemoteDataSource
    ): ForumApi {
        return remoteDataSource.buildApi(ForumApi::class.java, null, null)
    }

    @Provides
    fun provideNotificationsApi(
        remoteDataSource: RemoteDataSource
    ): NotificationsApi {
        return remoteDataSource.buildApi(NotificationsApi::class.java, null, null)
    }
}