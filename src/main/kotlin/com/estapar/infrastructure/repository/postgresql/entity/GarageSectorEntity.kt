package com.estapar.infrastructure.repository.postgresql.entity

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorStatus
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
    @Column("duration_limit_minutes") val durationLimitMinutes: Int,
    @Column("status") val status: String = SectorStatus.CLOSED.name
) {

    fun toDomain(): Sector =
        Sector(
            id = id,
            name = name,
            basePrice = basePrice,
            maxCapacity = maxCapacity,
            openHour = openHour,
            closeHour =  closeHour,
            durationLimitMinutes = durationLimitMinutes,
            status = SectorStatus.of(status))

    companion object {
        fun of(sector: Sector): GarageSectorEntity =
            GarageSectorEntity(
                id = sector.id,
                name = sector.name,
                basePrice = sector.basePrice,
                maxCapacity = sector.maxCapacity,
                openHour = sector.openHour,
                closeHour =  sector.closeHour,
                durationLimitMinutes = sector.durationLimitMinutes,
                status = sector.status.name)
    }

}
