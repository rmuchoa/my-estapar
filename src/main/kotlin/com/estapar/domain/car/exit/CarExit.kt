package com.estapar.domain.car.exit

import java.time.LocalDateTime

data class CarExit(
    val id: Long? = null,
    val licensePlate: String,
    val exitTime: LocalDateTime)
