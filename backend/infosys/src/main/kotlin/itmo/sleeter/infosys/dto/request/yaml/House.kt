package itmo.sleeter.infosys.dto.request.yaml

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class House(
    val name: String,
    @field:Min(1)
    @field:Max(901)
    val year: Int,
    @JsonProperty("number_of_lifts")
    @Min(1)
    val numberOfLifts: Long
)
