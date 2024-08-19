package com.book.warandpeace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.book.warandpeace.data.local.scores
import com.book.warandpeace.data.repository.WarAndPeaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class WarAndPeaceViewModel @Inject constructor(
    private val repository: WarAndPeaceRepository
) : ViewModel(
) {
    private val splitWords: List<String> by lazy {
        splitWords()
    }
    private val _uiState = MutableStateFlow<UIState>(UIState.Init)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    private var resultsFromLocalNetwork = ""

    var resultsFromLocalFile: Map<String, Int> = mapOf()

    fun getFile(textFileName: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            try {
                val textFile = repository.getTextFile(textFileName)
                resultsFromLocalNetwork = textFile
                _uiState.value = UIState.Success(textFile)
            } catch (e: Throwable) {
                handleException(e)
            }
        }
    }

    fun getMostFrequentWordFromLocalFile(textFileName: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            try {
                val result = repository.getMostFrequentWordsFromLocalFile(textFileName)
                resultsFromLocalFile = result
                _uiState.value = UIState.Success(result.toString())
            } catch (e: Throwable) {
                handleException(e)
            }
        }
    }

    fun isTextFile(fileName: String): Boolean {
        return fileName.trim().lowercase().endsWith(".txt")
    }

    private fun wordFrequency(): Map<String, Int> {
        val resulting = resultsFromLocalFile.ifEmpty {
            val words = splitWords
            words.groupBy {
                it
            }.mapValues { it.value.size }
                .toList()
                .sortedByDescending { it.second }
                .toMap()
        }
        return resulting
    }

    private fun splitWords(): List<String> {
        val nomarlizedWords = resultsFromLocalNetwork.normalizer()
        val splitWords = nomarlizedWords.split("\\s+".toRegex()).map { it.lowercase() }
        return splitWords
    }

    fun mostFrequentWord(): Map.Entry<String, Int>? {
        return wordFrequency().maxByOrNull { it.value }
    }

    fun mostFrequentSevenCharacterWord(): Map.Entry<String, Int>? {
        return wordFrequency().entries.firstOrNull { it.key.length == 7 }
    }

    suspend fun highestScoringWord(): Pair<String, Int> = withContext(Dispatchers.Default) {
        val words =
            if (resultsFromLocalFile.isEmpty()) splitWords else resultsFromLocalFile.keys.toList()
        var highest = 0
        var highestWord = ""

        for (word in words) {
            val total = word.sumOf { scores[it] ?: 0 }
            if (total > highest) {
                highestWord = word
                highest = total
            }
        }
        Pair(highestWord, highest)
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is HttpException -> {
                val errorCode = exception.code()
                when (errorCode) {
                    404 -> _uiState.value = UIState.Error("File Not Found")
                    else -> _uiState.value = UIState.Error("Internal Server Error")
                }
            }

            else -> {
                _uiState.value = UIState.Error("Unexpected Error: ${exception.message}")
            }
        }
    }

    @TestOnly
    fun setResultsFromLocal(results: Map<String, Int>) {
        resultsFromLocalFile = results
    }
}