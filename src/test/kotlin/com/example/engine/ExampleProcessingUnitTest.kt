package com.example.engine

import com.example.model.TimeSeriesData
import org.junit.Test
import org.junit.Assert.assertEquals

class ExampleProcessingUnitTest {
    @Test
    fun testExampleProcessingUnit() {
        val processingUnit = ExampleProcessingUnit()
        val input = TimeSeriesData(1, 2.0)
        val output = processingUnit.process(input)
        assertEquals(4.0, output, 0.0)
    }
}