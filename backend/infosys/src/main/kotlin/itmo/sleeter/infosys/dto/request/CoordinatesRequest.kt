package itmo.sleeter.infosys.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class CoordinatesRequest(
    @Max(598)
    val x : Long,
    @Min(-254)
    val y : Double
)
