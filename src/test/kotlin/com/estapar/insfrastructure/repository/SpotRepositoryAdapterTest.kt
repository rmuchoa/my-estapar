package com.estapar.insfrastructure.repository

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.spot.Spot
import com.estapar.infrastructure.repository.SectorRepositoryAdapter
import com.estapar.infrastructure.repository.SpotRepositoryAdapter
import com.estapar.infrastructure.repository.postgresql.entity.GarageSpotEntity
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
class SpotRepositoryAdapterTest {

    @Mock private lateinit var repository: JPAGarageSpotRepository
    @Mock private lateinit var sectorRepository: SectorRepositoryAdapter
    private val spotCaptor = argumentCaptor<GarageSpotEntity>()
    private lateinit var adapter: SpotRepositoryAdapter

    @BeforeEach
    fun setUp() {
        adapter = SpotRepositoryAdapter(repository, sectorRepository)
    }

    @Test
    fun shouldAskRepositoryToSaveSpotWhenSavingSpot() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closeHour = closeHour)
        val spot = Spot(
            sector = sector,
            latitude = 39.22,
            longitude = 45.23,
            occupied = true)
        `when`(repository.save(any())).thenReturn(Mono.empty())

        StepVerifier.create(adapter.save(spot))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(any())
    }

    @Test
    fun shouldConvertAndSendSpotToSaveOnRepositoryWhenSavingSpot() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            id = 1,
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closeHour = closeHour)
        val spot = Spot(
            sector = sector,
            latitude = 39.22,
            longitude = 45.23,
            occupied = true)
        `when`(repository.save(any())).thenReturn(Mono.empty())

        StepVerifier.create(adapter.save(spot))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(spotCaptor.capture())
        assertThat(spotCaptor.firstValue, allOf(
            instanceOf(GarageSpotEntity::class.java),
            hasProperty("id", nullValue()),
            hasProperty("sectorId", equalTo(sector.id)),
            hasProperty("latitude", equalTo(spot.latitude)),
            hasProperty("longitude", equalTo(spot.longitude)),
            hasProperty("occupied", equalTo(spot.occupied))
        ))
    }

    @Test
    fun shouldConvertAndReturnReceivedResponseWhenSavingSpot() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            id = 1,
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closeHour = closeHour)
        val spot = Spot(
            sector = sector,
            latitude = 39.22,
            longitude = 45.23,
            occupied = true)
        val entity = GarageSpotEntity(
            id = 2,
            sectorId = sector.id,
            latitude = spot.latitude,
            longitude = spot.longitude,
            occupied = spot.occupied
        )
        `when`(repository.save(any())).thenReturn(Mono.just(entity))

        StepVerifier.create(adapter.save(spot))
            .assertNext { spot ->
                assertThat(spot, allOf(
                    instanceOf(Spot::class.java),
                    hasProperty("id", equalTo(entity.id)),
                    hasProperty("sector", instanceOf<Sector>(Sector::class.java)),
                    hasProperty("latitude", equalTo(entity.latitude)),
                    hasProperty("longitude", equalTo(entity.longitude)),
                    hasProperty("occupied", equalTo(entity.occupied)),
                    )
                )
            }
            .verifyComplete()
    }

}