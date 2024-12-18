package itmo.sleeter.infosys.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class UpdateFlatRequest(
    @NotEmpty
    val name: String,
    @Min(1) @Max(724)
    val area: Double,
    @Min(1)
    val price: Double,
    val balcony: Boolean,
    @JsonProperty("time_to_metro_on_foot")
    @Min(1)
    val timeToMetroOnFoot: Double,
    @JsonProperty("number_of_rooms")
    @Min(1)
    val numberOfRooms: Long,
    val furnish: String,
    val view: String,
    val transport: String,
    val coordinate: CoordinatesRequest,
    @JsonProperty("house_id")
    @Min(0)
    val houseId: Long,
    @JsonProperty("user_id")
    @Min(0)
    val userId: Long
)