package com.estapar.infrastructure.repository.postgresql

import com.estapar.domain.garage.sector.Sector
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalTime

@Table(name = "garage_sector")
data class GarageSectorEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("name") val name: String,
    @Column("base_price") val basePrice: BigDecimal,
    @Column("max_capacity") val maxCapacity: Int,
    @Column("open_hour") val openHour: LocalTime,
    @Column("close_hour") val closeHour: LocalTime,
    @Column("duration_limit_minutes") val durationLimitMinutes: Int
) {

    fun toDomain(): Sector =
        Sector(
            id = id,
            name = name,
            basePrice = basePrice,
            maxCapacity = maxCapacity,
            openHour = openHour,
            closeHour =  closeHour,
            durationLimitMinutes = durationLimitMinutes)

    companion object {
        fun of(sector: Sector): GarageSectorEntity =
            GarageSectorEntity(
                name = sector.name,
                basePrice = sector.basePrice,
                maxCapacity = sector.maxCapacity,
                openHour = sector.openHour,
                closeHour =  sector.closeHour,
                durationLimitMinutes = sector.durationLimitMinutes)
    }

}
