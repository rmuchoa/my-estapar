package com.estapar.infrastructure.repository.postgresql.entity

import com.estapar.domain.car.GarageLoggingStatus
import com.estapar.domain.car.logging.GarageLogging
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "garage_logging")
data class GarageLoggingEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("license_plate") val licensePlate: String,
    @Column("entry_time") val entryTime: LocalDateTime,
    @Column("exit_time") val exit: LocalDateTime? = null,
    @Column("status") val status: String = GarageLoggingStatus.ACTIVE.name
) {

    fun toDomain(): GarageLogging =
        GarageLogging(
            id = id,
            entryTime = entryTime,
            licensePlate = licensePlate)

    companion object {
        fun of(garageLogging: GarageLogging): GarageLoggingEntity =
            GarageLoggingEntity(
                id = garageLogging.id,
                entryTime = garageLogging.entryTime,
                licensePlate = garageLogging.licensePlate)
    }

}
