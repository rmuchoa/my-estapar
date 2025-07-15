package com.estapar.infrastructure.repository.postgresql

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.spot.Spot
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "garage_spot")
data class GarageSpotEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("sector_id") val sectorId: Long?,
    @Column("latitude") val latitude: Double,
    @Column("longitude") val longitude: Double,
    @Column("occupied") val occupied: Boolean
) {

    fun toDomain(sector: Sector? = null): Spot =
        Spot(
            id = id,
            sector = sector,
            latitude = latitude,
            longitude = longitude,
            occupied = occupied)

    companion object {
        fun of(spot: Spot): GarageSpotEntity =
            GarageSpotEntity(
                id = spot.id,
                sectorId = spot.sector?.id,
                latitude = spot.latitude,
                longitude = spot.longitude,
                occupied = spot.occupied)
    }
}