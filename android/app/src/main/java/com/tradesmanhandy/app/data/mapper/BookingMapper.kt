package com.tradesmanhandy.app.data.mapper

import com.tradesmanhandy.app.data.model.Booking as DataBooking
import com.tradesmanhandy.app.data.model.BookingStatus as DataBookingStatus
import com.tradesmanhandy.app.domain.model.Booking as DomainBooking
import com.tradesmanhandy.app.domain.model.BookingStatus as DomainBookingStatus

fun DataBooking.toDomain(): DomainBooking {
    return DomainBooking(
        id = id,
        title = title,
        description = description,
        location = location,
        quotedPrice = quotedPrice,
        scheduledDate = scheduledDate,
        status = when (status) {
            DataBookingStatus.PENDING -> DomainBookingStatus.PENDING
            DataBookingStatus.ACCEPTED, DataBookingStatus.CONFIRMED -> DomainBookingStatus.CONFIRMED
            DataBookingStatus.COMPLETED -> DomainBookingStatus.COMPLETED
            DataBookingStatus.REJECTED, DataBookingStatus.CANCELLED -> DomainBookingStatus.REJECTED
        }
    )
}

fun List<DataBooking>.toDomainList(): List<DomainBooking> = map { it.toDomain() }
