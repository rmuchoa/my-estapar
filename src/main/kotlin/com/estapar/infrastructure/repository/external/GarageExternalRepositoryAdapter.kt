package com.estapar.infrastructure.repository.external

import com.estapar.domain.garage.Garage
import com.estapar.domain.garage.GarageExternalRepository
import com.estapar.infrastructure.client.GarageWebClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GarageExternalRepositoryAdapter(
    private val webClient: GarageWebClient
) : GarageExternalRepository {

    override fun loadGarageFirstTime(): Mono<Garage> =
        webClient.getFirstGarageLoad()
            .map { it.toDomain() }
}