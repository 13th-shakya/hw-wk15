package com.example.kotlinhomework6

data class AirQualityObject(
    val result: Result
) {
    data class Result(
        val records: List<Record>
    ) {
        data class Record(
            val SiteName: String,
            val Status: String,
        )
    }
}
