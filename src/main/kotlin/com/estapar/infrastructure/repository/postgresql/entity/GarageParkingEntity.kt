package com.estapar.infrastructure.repository.postgresql.entity

import com.estapar.domain.car.park.DynamicPriceRule
import com.estapar.domain.car.park.Parking
import com.estapar.domain.car.park.ParkingStatus
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
    @Column("price_level_rate") val priceLevelRate: Int,
    @Column("parking_time") val parkingTime: LocalDateTime,
    @Column("unparking_time") val unparkingTime: LocalDateTime?,
    @Column("status") val status: String = ParkingStatus.ENTERED.name
) {

    fun toDomain(spot: Spot): Parking =
        Parking(
            id = id,
            spot = spot,
            licensePlate = licensePlate,
            parkingTime = parkingTime,
            unparkingTime = unparkingTime,
            priceRule = DynamicPriceRule.of(priceLevelRate = priceLevelRate),
            status = ParkingStatus.valueOf(status))

    companion object {
        fun of(parking: Parking): GarageParkingEntity =
            GarageParkingEntity(
                id = parking.id,
                spotId = parking.spot.id!!,
                licensePlate = parking.licensePlate,
                priceLevelRate = parking.priceRule.priceRate,
                parkingTime = parking.parkingTime,
                unparkingTime = parking.unparkingTime,
                status = parking.status.name)
    }
}
