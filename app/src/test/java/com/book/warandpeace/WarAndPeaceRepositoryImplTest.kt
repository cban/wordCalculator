package com.book.warandpeace

import com.book.warandpeace.data.local.FileReader
import com.book.warandpeace.data.network.RemoteApi
import com.book.warandpeace.data.repository.WarAndPeaceRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class WarAndPeaceRepositoryImplTest {

    private lateinit var subjectUnderTest: WarAndPeaceRepositoryImpl
    private val apiService: RemoteApi = Mockito.mock(RemoteApi::class.java)
    private val fileReader: FileReader = Mockito.mock(FileReader::class.java)

    @Before
    fun setUp() {
        subjectUnderTest = WarAndPeaceRepositoryImpl(
            apiService,
            fileReader
        )
    }

    @Test
    fun `verify WarAndPeaceRepositoryImpl getTextFile returns a textFile`() = runTest {
        val textFileName = "2300-03.txt"
        val expectedUrl = "2600/$textFileName"
        val fileContent = "The Project Gutenberg eBook of War and Peace, by Leo Tolstoy."

        `when`(apiService.getTextFile(expectedUrl)).thenReturn(
            fileContent
        )
        val result = subjectUnderTest.getTextFile(textFileName)

        assertTrue(result.isNotEmpty())
        verify(apiService).getTextFile(expectedUrl)

    }

    @Test
    fun `verify WarAndPeaceRepositoryImpl getTextFile returns the correct textFile`() = runTest {
        val textFileName = "2300-03.txt"
        val expectedUrl = "2600/$textFileName"
        val expectedFileContent = "In a moment. À propos,” she added, becoming calm again"

        `when`(apiService.getTextFile(expectedUrl)).thenReturn(
            expectedFileContent
        )

        val result = subjectUnderTest.getTextFile(textFileName)

        assertEquals(expectedFileContent, result)
        verify(apiService).getTextFile(expectedUrl)

    }

    @Test
    fun `verify WarAndPeaceRepositoryImpl getMostFrequentWordsFromLocalFile  returns the correct frequent words`() =
        runTest {
            val expectedWordFrequency = mutableMapOf("one" to 3, "the" to 10, "coding" to 6)
            val textFileUrl = "2300-03.txt"
            `when`(fileReader.getWordFrequencies(textFileUrl)).thenReturn(
                expectedWordFrequency
            )
            val result = subjectUnderTest.getMostFrequentWordsFromLocalFile(textFileUrl)
            assertEquals(expectedWordFrequency, result)
            verify(fileReader).getWordFrequencies(textFileUrl)
        }
}