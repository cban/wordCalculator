package com.book.warandpeace.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilTest {

    @Test
    fun `normalizer should remove accents and convert to lowercase`() {

        val testCases = mapOf(
            "Vasíli" to "vasili",
            "Catherine" to "catherine",
            "Natásha" to "natasha",
            "Mikháylovna" to "mikhaylovna",
            "Ivánovich" to "ivanovich"
        )

        for ((input, expected) in testCases) {
            assertEquals(expected, input.normalizer())
        }
    }
}