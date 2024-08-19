package com.book.warandpeace.di

import com.book.warandpeace.data.network.RemoteApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun provideHttpLoggingInterceptor(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideLoginInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideApiService(
    ): RemoteApi {
        return Retrofit.Builder()
            .baseUrl("https://www.gutenberg.org/files/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(provideHttpLoggingInterceptor(provideLoginInterceptor()))
            .build()
            .create(RemoteApi::class.java)
    }
}
