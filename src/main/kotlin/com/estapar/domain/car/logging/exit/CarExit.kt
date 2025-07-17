package com.estapar.domain.car.logging.exit

import java.time.LocalDateTime

data class CarExit(
    val licensePlate: String,
    val exitTime: LocalDateTime)
