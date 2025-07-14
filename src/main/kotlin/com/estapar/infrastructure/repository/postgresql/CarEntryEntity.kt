package com.estapar.infrastructure.repository.postgresql

import com.estapar.domain.garage.carEntry.CarEntry
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "car_entry")
data class CarEntryEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("license_plate") val licensePlate: String,
    @Column("entry_time") val entryTime: LocalDateTime
) {

    fun toDomain(): CarEntry =
        CarEntry(
            licensePlate = licensePlate,
            entryTime = entryTime)

    companion object {
        fun of(carEntry: CarEntry): CarEntryEntity =
            CarEntryEntity(
                licensePlate = carEntry.licensePlate,
                entryTime = carEntry.entryTime)
    }

}
