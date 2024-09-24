package com.example.esper

import com.espertech.esper.common.client.configuration.Configuration
import com.espertech.esper.runtime.client.EPRuntimeProvider
import com.example.model.TimeSeriesData

object EsperConfig {
    val config: Configuration = Configuration().apply {
        common.addEventType("TimeSeriesData", TimeSeriesData::class.java)
    }
    val runtime = EPRuntimeProvider.getDefaultRuntime()

    init {
        runtime.initialize()
    }
}