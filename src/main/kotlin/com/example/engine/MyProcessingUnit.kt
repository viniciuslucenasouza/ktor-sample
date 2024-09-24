package com.example.engine

import com.example.model.TimeSeriesData
import com.example.model.RealTimeData
import com.example.esper.EsperConfig
class MyProcessingUnit {
    private val runtime = EsperConfig.runtime

    fun processTimeSeriesData(data: TimeSeriesData) {
        runtime.eventService.sendEventBean(data, "TimeSeriesData")
    }
    fun ProcessRealTimeData(data:RealTimeData) {
        EsperConfig.runtime.eventService.sendEventBean(data, "RealTimeData")
    }
}