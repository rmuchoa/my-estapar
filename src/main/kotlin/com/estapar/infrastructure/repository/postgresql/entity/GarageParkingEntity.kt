package com.estapar.infrastructure.repository.postgresql.entity

import com.estapar.domain.car.park.Parking
import com.estapar.domain.garage.spot.Spot
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "garage_parking")
data class GarageParkingEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("spot_id") val spotId: Long,
    @Column("license_plate") val licensePlate: String,
    @Column("parking_time") val parkingTime: LocalDateTime,
    @Column("price_level_rate") val priceLevelRate: Int,
) {

    fun toDomain(spot: Spot): Parking =
        Parking(
            id = id,
            spot = spot,
            licensePlate = licensePlate,
            parkingTime = parkingTime)

    companion object {
        fun of(parking: Parking): GarageParkingEntity =
            GarageParkingEntity(
                id = parking.id,
                spotId = parking.spot.id!!,
                licensePlate = parking.licensePlate,
                parkingTime = parking.parkingTime,
                priceLevelRate = parking.priceRule.priceRate)
    }
}
