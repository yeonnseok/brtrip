package com.brtrip.trip.controller.response

import com.brtrip.common.utils.format_yyyy_MM_dd
import com.brtrip.trip.domain.Trip

data class TripResponse(
    val title: String,
    val startDate: String,
    val endDate: String,
    val memo: String?,
    val likeCount: Long
) {
    companion object {
        fun of(trip: Trip): TripResponse {
            return TripResponse(
                title = trip.title,
                startDate = trip.startDate.format_yyyy_MM_dd(),
                endDate = trip.endDate.format_yyyy_MM_dd(),
                memo = trip.memo,
                likeCount = trip.likeCount
            )
        }
    }
}
