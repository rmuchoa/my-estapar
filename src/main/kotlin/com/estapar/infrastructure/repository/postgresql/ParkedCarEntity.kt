package com.estapar.infrastructure.repository.postgresql

import com.estapar.domain.garage.carEntry.CarEntry
import com.estapar.domain.garage.park.ParkedCar
import com.estapar.domain.garage.spot.Spot
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "parked_car")
data class ParkedCarEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("spot_id") val spotId: Long,
    @Column("car_entry_id") val carEntryId: Long,
    @Column("license_plate") val licensePlate: String,
    @Column("parking_time") val parkingTime: LocalDateTime,
) {

    fun toDomain(
        spot: Spot,
        carEntry: CarEntry): ParkedCar =
        ParkedCar(
            id = id,
            spot = spot,
            carEntry = carEntry,
            licensePlate = licensePlate,
            parkingTime = parkingTime)

    companion object {
        fun of(parkedCar: ParkedCar): ParkedCarEntity =
            ParkedCarEntity(
                id = parkedCar.id,
                spotId = parkedCar.spot.id!!,
                carEntryId = parkedCar.carEntry.id!!,
                licensePlate = parkedCar.licensePlate,
                parkingTime = parkedCar.parkingTime)
    }
}
