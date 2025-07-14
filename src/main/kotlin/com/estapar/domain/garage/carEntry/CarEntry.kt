package com.estapar.domain.garage.carEntry

import java.time.LocalDateTime

data class CarEntry(
    val licensePlate: String,
    val entryTime: LocalDateTime)
