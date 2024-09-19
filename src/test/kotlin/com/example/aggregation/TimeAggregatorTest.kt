package com.example.aggregation

import com.example.model.TimeSeriesData
import org.junit.Test
import org.junit.Assert.assertEquals

class TimeAggregatorTest {

    /**
     * Test the addData method, should increment the size of the data list
     * **/
    @Test
    fun testAddData() {

        val timeAggregator = TimeAggregator()
        timeAggregator.addData(TimeSeriesData(1, 2.0))
        assertEquals(1, timeAggregator.data.size)
        timeAggregator.addData(TimeSeriesData(1, 3.0))
        assertEquals(2, timeAggregator.data.size)
    }


    /**
     * Test the Aggregate method, should sum the values of the data list
     * **/
    @Test
    fun testAggregate() {
        val timeAggregator = TimeAggregator()
        timeAggregator.addData(TimeSeriesData(1, 2.0))
        timeAggregator.addData(TimeSeriesData(1, 3.0))
        assertEquals(5.0, timeAggregator.aggregate(), 0.0)
    }
}