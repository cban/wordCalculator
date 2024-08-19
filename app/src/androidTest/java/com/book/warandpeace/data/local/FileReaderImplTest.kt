package com.book.warandpeace.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class FileReaderImplTest {

    private lateinit var subjectUnderTest: FileReaderImpl
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        subjectUnderTest = FileReaderImpl(context)
    }

    @Test
    fun getWordFrequenciesFromTestFileShouldReturnCorrectValues() = runTest {
        val filePath = "test.txt"

        val expectedFrequencies = mutableMapOf(
            "one" to 5,
            "two" to 2,
            "natasha" to 3,
            "catherine" to 2
        )
        val actualFrequencies = subjectUnderTest.getWordFrequencies(filePath)
        assertEquals(expectedFrequencies, actualFrequencies)
    }
}