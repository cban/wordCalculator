package com.book.warandpeace.di

import com.book.warandpeace.data.local.FileReader
import com.book.warandpeace.data.network.RemoteApi
import com.book.warandpeace.data.repository.WarAndPeaceRepository
import com.book.warandpeace.data.repository.WarAndPeaceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [(RemoteModule::class)])
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWarAndPeaceRepository(
        apiService: RemoteApi,
        localFileReader: FileReader
    ): WarAndPeaceRepository {
        return WarAndPeaceRepositoryImpl(
            apiService, localFileReader
        )
    }
}