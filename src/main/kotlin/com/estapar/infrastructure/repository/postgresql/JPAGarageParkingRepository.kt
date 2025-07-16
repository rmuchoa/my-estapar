package com.estapar.infrastructure.repository.postgresql

import com.estapar.infrastructure.repository.postgresql.entity.GarageParkingEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JPAGarageParkingRepository : ReactiveCrudRepository<GarageParkingEntity, Long>