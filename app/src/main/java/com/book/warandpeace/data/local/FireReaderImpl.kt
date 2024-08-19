package com.book.warandpeace.data.local

import android.app.Application
import android.content.Context
import com.book.warandpeace.data.network.RemoteApi
import com.book.warandpeace.ui.normalizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.inject.Inject

class FileReaderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileReader {
    override suspend fun getWordFrequencies(filePath: String): MutableMap<String, Int> =
        withContext(Dispatchers.IO) {
            val wordCounts = mutableMapOf<String, Int>()
            context.assets.open(filePath).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { bufferedReader ->
                    bufferedReader.forEachLine { line ->

                        val words =
                            line.normalizer().split("\\s+".toRegex())
                        for (word in words) {
                            val cleanedWord = word.lowercase().trim()
                            if (cleanedWord.isNotEmpty()) {
                                wordCounts[cleanedWord] =
                                    wordCounts.getOrDefault(cleanedWord, 0) + 1
                            }
                        }
                    }
                }
            }
            val sortedByDescendingValues = wordCounts
                .toList()
                .sortedByDescending { (_, value) -> value }
                .toMap()
            sortedByDescendingValues.toMutableMap()
        }

}
