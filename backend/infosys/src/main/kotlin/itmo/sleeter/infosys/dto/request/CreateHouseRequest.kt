package itmo.sleeter.infosys.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull

data class CreateHouseRequest(
    @NotNull
    val name: String,
    @JsonProperty("number_of_lifts")
    @Min(1)
    val numberOfLifts: Long,
    @Min(1) @Max(901)
    val year: Int,
    @JsonProperty("user_id")
    @Min(0)
    val userId: Long
)
