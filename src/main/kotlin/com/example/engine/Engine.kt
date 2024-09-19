package com.example.engine

import com.example.model.TimeSeriesData

// this is an interface to processes the boxes (units of processing)
interface ProcessingUnit<I, O> {
    fun process(input: I): O
}

// This is a base class for processing the units
abstract class BaseProcessingUnit<I, O> : ProcessingUnit<I, O> {
    abstract override fun process(input: I): O
}

// This is an example processing unit that processes the timeseries data
// each specific processing units should be created for different type of analytics

class ExampleProcessingUnit : BaseProcessingUnit<TimeSeriesData, Double>() {
    override fun process(input: TimeSeriesData): Double {
        // Example processing logic
        return input.value * 2
    }
}