package itmo.sleeter.infosys.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class HouseResponse(
    val id : Long,
    @JsonProperty("number_of_lifts")
    val numberOfLifts : Long,
    val year : Int,
    @JsonProperty("created_at")
    val createdAt : Instant,
    @JsonProperty("updated_at")
    val updatedAt : Instant,
    @JsonProperty("user_create")
    val userCreate : UserResponse,
    @JsonProperty("user_update")
    val userUpdate : UserResponse
)