package com.estapar.insfrastructure.repository

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorStatus
import com.estapar.infrastructure.repository.SectorRepositoryAdapter
import com.estapar.infrastructure.repository.postgresql.entity.GarageSectorEntity
import com.estapar.infrastructure.repository.postgresql.entity.GarageSpotEntity
import com.estapar.infrastructure.repository.postgresql.JPAGarageSectorRepository
import com.estapar.infrastructure.repository.postgresql.JPAGarageSpotRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class SectorRepositoryAdapterTest(
    @Mock private val repository: JPAGarageSectorRepository,
    @Mock private val spotRepository: JPAGarageSpotRepository
) {

    private val sectorCaptor = argumentCaptor<GarageSectorEntity>()
    private lateinit var adapter: SectorRepositoryAdapter

    @BeforeEach
    fun setUp() {
        adapter = SectorRepositoryAdapter(repository, spotRepository)
    }

    @Test
    fun shouldAskRepositoryToSaveSectorWhenSavingSector() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closedHour = closeHour)
        `when`(repository.save(any())).thenReturn(Mono.empty())

        StepVerifier.create(adapter.save(sector))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(any())
    }

    @Test
    fun shouldConvertAndSendSectorToSaveOnRepositoryWhenSavingSector() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closedHour = closeHour)
        `when`(repository.save(any())).thenReturn(Mono.empty())

        StepVerifier.create(adapter.save(sector))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(sectorCaptor.capture())
        assertThat(sectorCaptor.firstValue, allOf(
            instanceOf(GarageSectorEntity::class.java),
            hasProperty("id", nullValue()),
            hasProperty("name", equalTo(sector.name)),
            hasProperty("basePrice", equalTo(sector.basePrice)),
            hasProperty("maxCapacity", equalTo(sector.maxCapacity)),
            hasProperty("durationLimitMinutes", equalTo(sector.durationLimitMinutes)),
            hasProperty("openHour", equalTo(sector.openHour)),
            hasProperty("closeHour", equalTo(sector.closedHour))
        ))
    }

    @Test
    fun shouldConvertAndReturnReceivedResponseWhenSavingSector() {
        val sectorId = 3L
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closedHour = closeHour)
        val entity = GarageSectorEntity(
            id = 2,
            name = sector.name,
            basePrice = sector.basePrice,
            maxCapacity = sector.maxCapacity,
            durationLimitMinutes = sector.durationLimitMinutes,
            openHour = sector.openHour,
            closedHour = sector.closedHour
        )
        val spotEntity = GarageSpotEntity(
            id = 2,
            sectorId = sector.id,
            latitude = 20.23,
            longitude = 45.32,
            occupied = false
        )
        `when`(repository.save(any())).thenReturn(Mono.just(entity.copy(id = sectorId)))

        StepVerifier.create(adapter.save(sector))
            .assertNext { sector ->
                assertThat(sector, allOf(
                    instanceOf(Sector::class.java),
                    hasProperty("id", equalTo(sectorId)),
                    hasProperty("name", equalTo(entity.name)),
                    hasProperty("basePrice", equalTo(entity.basePrice)),
                    hasProperty("maxCapacity", equalTo(entity.maxCapacity)),
                    hasProperty("durationLimitMinutes", equalTo(entity.durationLimitMinutes)),
                    hasProperty("openHour", equalTo(entity.openHour)),
                    hasProperty("closeHour", equalTo(entity.closedHour)),
                    hasProperty("status", equalTo(SectorStatus.CLOSED))
                ))
            }
            .verifyComplete()
    }

}