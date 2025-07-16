package com.estapar.domain.garage

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.spot.Spot

data class Garage(
    val sectors: List<Sector>,
    val spots: List<Spot>
) {

    fun applySpotSectors(sectors: List<Sector>): Garage {
        return copy(sectors = sectors, spots = this.spots.map {
            spot -> spot.copy(sector = sectors.find {
                sector -> sector.name.equals(other = spot.sector?.name, ignoreCase = true)
            })
        })
    }

}