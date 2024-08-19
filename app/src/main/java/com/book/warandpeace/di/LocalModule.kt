package com.book.warandpeace.di

import android.content.Context
import com.book.warandpeace.WarAndPeaceApplication
import com.book.warandpeace.data.local.FileReader
import com.book.warandpeace.data.local.FileReaderImpl
import com.book.warandpeace.data.network.RemoteApi
import com.book.warandpeace.data.repository.WarAndPeaceRepository
import com.book.warandpeace.data.repository.WarAndPeaceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideFileReader(
        @ApplicationContext context: Context
    ): FileReader {
        return FileReaderImpl(
            context
        )
    }
}