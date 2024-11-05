package itmo.sleeter.infosys.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateFlatRequest(
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
    val coordinate: CoordinatesRequest,
    @JsonProperty("house_id")
    val houseId: Long,
    @JsonProperty("user_id")
    val userId: Long
)