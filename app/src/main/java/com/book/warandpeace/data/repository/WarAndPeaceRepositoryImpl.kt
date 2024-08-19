package com.book.warandpeace.data.repository

import com.book.warandpeace.data.local.FileReader
import com.book.warandpeace.data.network.RemoteApi
import javax.inject.Inject

class WarAndPeaceRepositoryImpl @Inject constructor(
    private val networkDataSource: RemoteApi,
    private val localDataSource: FileReader
) : WarAndPeaceRepository {
    override suspend fun getTextFile(fileUrl: String): String {
        /* explicitly added "2600/" in the url path for the purpose of this exercise so that we just look in one directory
        otherwise a more general url is better to search in different directories */
        return networkDataSource.getTextFile("2600/$fileUrl")
    }

    override suspend fun getMostFrequentWordsFromLocalFile(fileUrl: String): MutableMap<String, Int> {
        return localDataSource.getWordFrequencies(fileUrl)
    }
}

