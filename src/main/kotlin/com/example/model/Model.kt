package com.example.model

data class TimeSeriesData(val timestamp: Long, val value: Double)
data class RealTimeData(val id: String, val payload: String)

// creating a class for timeseries data and realtime streaming data
// i am defining onterfaces for datasources and sinks

interface DataSource<T> {
    fun fetchData(): List<T>
}

interface DataSink<T> {
    fun sendData(data: T)
}