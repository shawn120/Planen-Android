package com.example.planmanager.data

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OAuthService {

    @FormUrlEncoded
    @POST("/token")
    fun getToken(
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String
    ): Call<GoogleTokenResponse> // Define a model class to represent the response

}