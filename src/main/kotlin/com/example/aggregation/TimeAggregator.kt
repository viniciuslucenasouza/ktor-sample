package com.example.aggregation

import com.example.model.TimeSeriesData

class TimeAggregator {
    val data = mutableListOf<TimeSeriesData>()

    fun addData(newData: TimeSeriesData) {
        data.add(newData)
    }

    fun aggregate(): Double {
        return data.sumOf { it.value }
    }
}