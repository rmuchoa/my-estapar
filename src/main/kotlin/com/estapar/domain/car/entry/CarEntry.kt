package com.estapar.domain.car.entry

import java.time.LocalDateTime

data class CarEntry(
    val id: Long? = null,
    val licensePlate: String,
    val entryTime: LocalDateTime)
