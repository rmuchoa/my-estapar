package com.estapar.infrastructure.repository.postgresql.entity

import com.estapar.domain.carEntry.CarEntry
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "car_entry")
data class CarEntryEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("entry_time") val entryTime: LocalDateTime,
    @Column("license_plate") val licensePlate: String
) {

    fun toDomain(): CarEntry =
        CarEntry(
            id = id,
            entryTime = entryTime,
            licensePlate = licensePlate)

    companion object {
        fun of(carEntry: CarEntry): CarEntryEntity =
            CarEntryEntity(
                id = carEntry.id,
                entryTime = carEntry.entryTime,
                licensePlate = carEntry.licensePlate)
    }

}
