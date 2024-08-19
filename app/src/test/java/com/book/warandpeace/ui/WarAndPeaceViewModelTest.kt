package com.book.warandpeace.ui

import MainDispatcherRule
import com.book.warandpeace.data.repository.WarAndPeaceRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class WarAndPeaceViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var subjectUnderTest: WarAndPeaceViewModel

    private val warAndPeaceRepository = Mockito.mock(WarAndPeaceRepository::class.java)

    @Before
    fun setUp() {
        subjectUnderTest = WarAndPeaceViewModel(
            warAndPeaceRepository
        )
    }

    @Test
    fun `getFile should emit success data when getFile returns data`() = runTest {
        val expected = UIState.Success("This is a test file")
        val url = "2026.txt"
        val mockTextFile = "This is a test file"
        `when`(warAndPeaceRepository.getTextFile(url)).thenReturn(mockTextFile)

        subjectUnderTest.getFile(url)

        verify(warAndPeaceRepository).getTextFile(url)
        assertEquals(expected, subjectUnderTest.uiState.value)
    }

    @Test
    fun `getFile should emit error data when getFile throws an exception or fails`() = runTest {

        val expected = UIState.Error("Unexpected Error: This is an exception")
        val url = "2026.txt"

        `when`(warAndPeaceRepository.getTextFile(url)).then {
            error("This is an exception")
        }
        subjectUnderTest.getFile(url)

        verify(warAndPeaceRepository).getTextFile(url)
        assertEquals(expected, subjectUnderTest.uiState.value)
    }

    @Test
    fun `getFile should emit success data when getMostFrequentWordFromLocalFile returns data`() =
        runTest {
            val data = mutableMapOf("one" to 3, "natasha" to 3, "catherine" to 2)
            val url = "2026.txt"
            val expected = UIState.Success(data.toString())

            `when`(warAndPeaceRepository.getMostFrequentWordsFromLocalFile(url))
                .thenReturn(data)

            subjectUnderTest.getMostFrequentWordFromLocalFile(url)

            verify(warAndPeaceRepository).getMostFrequentWordsFromLocalFile(url)
            assertEquals(expected, subjectUnderTest.uiState.value)
        }

    @Test
    fun `getFile should emit error data when getMostFrequentWordFromLocalFile throws an exception or fails`() =
        runTest {
            val url = "2026.txt"
            val expected = UIState.Error("Unexpected Error: This is an exception")

            `when`(warAndPeaceRepository.getMostFrequentWordsFromLocalFile(url))
                .then {
                    error("This is an exception")
                }

            subjectUnderTest.getMostFrequentWordFromLocalFile(url)

            verify(warAndPeaceRepository).getMostFrequentWordsFromLocalFile(url)
            assertEquals(expected, subjectUnderTest.uiState.value)
        }

    @Test
    fun `isTextFile should return true for filename ending with txt`() {
        val testCases = listOf(
            "document.txt",
            "test.TXT",
            "android.txt",
        )

        for (input in testCases) {
            val result = subjectUnderTest.isTextFile(input)
            assertTrue(result)
        }
    }

    @Test
    fun `isTextFile should return false for filename not ending with txt`() {
        val textFileName = "test"

        val result = subjectUnderTest.isTextFile(textFileName)

        assertFalse(result)
    }


    @Test
    fun `mostFrequentWord should return the word with the highest frequency`() {
        val mockFrequencyMap = mapOf("hello" to 5, "world" to 3, "test" to 7)
        subjectUnderTest.setResultsFromLocal(mockFrequencyMap)

        val result = subjectUnderTest.mostFrequentWord()

        assertEquals("test", result?.key)
    }

    @Test
    fun `mostFrequentSevenCharacterWord  should return the correct  result`() {
        val mockFrequencyMap = mapOf("natasha" to 5, "vehicle" to 3, "windows" to 7)

        subjectUnderTest.setResultsFromLocal(mockFrequencyMap)

        val result = subjectUnderTest.mostFrequentWord()

        assertEquals("windows", result?.key)
    }

    @Test
    fun `highestScoringWord should return the word with the highest score`() = runTest {

        subjectUnderTest.resultsFromLocalFile = mapOf("dog" to 1, "hello" to 2)

        val result = subjectUnderTest.highestScoringWord()

        assertEquals(Pair("hello", 8), result)
    }


}