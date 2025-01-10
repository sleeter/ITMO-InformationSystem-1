package itmo.sleeter.infosys.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ImportResponse(
    val id: Long,
    val status: Boolean,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("count_of_objects")
    val count: Int? = null,
)
