package itmo.sleeter.infosys.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class FlatResponse(
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
    val house: HouseResponseWithoutFlats,
    @JsonProperty("creation_date")
    val creationDate: Instant,
    @JsonProperty("updated_at")
    val updatedAt: Instant,
    @JsonProperty("user_create")
    val userCreate: UserResponse,
    @JsonProperty("user_update")
    val userUpdate: UserResponse,
    @JsonProperty("is_mine")
    val isMine: Boolean
)
