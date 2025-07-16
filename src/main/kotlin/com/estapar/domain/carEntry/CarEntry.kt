package com.estapar.domain.carEntry

import java.time.LocalDateTime

data class CarEntry(
    val id: Long? = null,
    val licensePlate: String,
    val entryTime: LocalDateTime)
