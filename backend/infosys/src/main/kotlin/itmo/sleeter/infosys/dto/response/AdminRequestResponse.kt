package itmo.sleeter.infosys.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AdminRequestResponse(
    val id: Long? = null,
    @JsonProperty("user_login")
    val userLogin: String? = null,
    @JsonProperty("admin_login")
    val adminLogin: String? = null,
)
