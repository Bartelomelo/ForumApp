package com.example.froumapp.network

import android.provider.ContactsContract.CommonDataKinds.Email
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ForumApi {

    @FormUrlEncoded
    @POST("")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Any
}