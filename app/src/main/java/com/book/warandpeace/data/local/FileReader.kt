package com.book.warandpeace.data.local


interface FileReader {
    suspend fun getWordFrequencies(filePath: String): MutableMap<String, Int>
}