package com.estapar.insfrastructure.repository

import com.estapar.AbstractEstaparTest
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

@ExtendWith(MockitoExtension::class)
class SpotRepositoryAdapterTest(
    @Mock private val repository: JPAGarageSpotRepository,
    @Mock private val sectorRepository: SectorRepositoryAdapter
) : AbstractEstaparTest() {

    private val spotCaptor = argumentCaptor<GarageSpotEntity>()
    private lateinit var adapter: SpotRepositoryAdapter

    @BeforeEach
    fun setUp() {
        adapter = SpotRepositoryAdapter(repository, sectorRepository)
    }

    @Test
    fun shouldAskRepositoryToSaveSpotWhenSavingSpot() {
        val spot = buildSomeSpot()
        `when`(repository.save(any())).thenReturn(Mono.empty())

        StepVerifier.create(adapter.save(spot))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(any())
    }

    @Test
    fun shouldConvertAndSendSpotToSaveOnRepositoryWhenSavingSpot() {
        val sector = buildSomeSector()
        val spot = buildSomeSpot(sector = sector)
        `when`(repository.save(any())).thenReturn(Mono.empty())

        StepVerifier.create(adapter.save(spot))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(spotCaptor.capture())
        assertThat(spotCaptor.firstValue, allOf(
            instanceOf(GarageSpotEntity::class.java),
            hasProperty("id", equalTo(spot.id)),
            hasProperty("sectorId", equalTo(sector.id)),
            hasProperty("latitude", equalTo(spot.latitude)),
            hasProperty("longitude", equalTo(spot.longitude)),
            hasProperty("occupied", equalTo(spot.occupied))
        ))
    }

    @Test
    fun shouldConvertAndReturnReceivedResponseWhenSavingSpot() {
        val spot = buildSomeSpot()
        val entity = buildSomeGarageSpotEntity(spot = spot)
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