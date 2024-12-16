package itmo.sleeter.infosys.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class FlatResponseWithoutHouse(
    val id: Long,
    val name: String,
    val area: Double,
    val price: Double,
    val balcony: Boolean,
    @JsonProperty("time_to_metro_on_foot")
    val timeToMetroOnFoot: Double,
    @JsonProperty("number_of_rooms")
    val numberOfRooms: Long,
    val furnish: String,
    val view: String,
    val transport: String,
    val coordinates: CoordinateResponse,
)
