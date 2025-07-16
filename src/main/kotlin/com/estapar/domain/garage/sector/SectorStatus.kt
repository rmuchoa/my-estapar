package com.estapar.domain.garage.sector

enum class SectorStatus {

    OPENED,
    CLOSED;

    companion object {
        fun of(status: String): SectorStatus =
            entries.find { type ->
                type.name == status
            }?: CLOSED
    }

}