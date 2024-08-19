package com.book.warandpeace.data.repository

interface WarAndPeaceRepository {

    suspend fun getTextFile(fileUrl: String): String
    suspend fun getMostFrequentWordsFromLocalFile(fileUrl: String): MutableMap<String, Int>
}