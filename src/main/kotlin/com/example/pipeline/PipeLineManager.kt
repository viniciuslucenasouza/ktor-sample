package com.example.pipeline

import com.example.engine.ProcessingUnit

class PipelineManager<I, O>(private val processingUnits: List<ProcessingUnit<I, O>>) {
    fun execute(input: I): O {
        var currentData: I = input
        for (unit in processingUnits) {
            currentData = unit.process(currentData) as I
        }
        return currentData as O
    }
}