package com.book.warandpeace.data.network

import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteApi {
    @GET
    suspend fun getTextFile(@Url fileUrl: String): String
}