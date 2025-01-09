package itmo.sleeter.infosys.dto.request.yaml

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class Flat(
    @NotEmpty
    val name: String,
    @JsonProperty("coordinates_id")
    val coordinatesId: Long? = null,
    val coordinates: List<Coordinate>? = null,
    @Min(1)
    @Max(724)
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
    @JsonProperty("house_id")
    val houseId: Long? = null,
    val house: List<House>? = null
)
