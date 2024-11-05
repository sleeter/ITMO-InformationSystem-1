package itmo.sleeter.infosys.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateHouseRequest(
    val name: String,
    @JsonProperty("number_of_lifts")
    val numberOfLifts: Long,
    val year: Int,
    @JsonProperty("user_id")
    val userId: Long
)
