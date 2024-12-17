package itmo.sleeter.infosys.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class HouseFilter (
    val name: String? = null,
    @JsonProperty("number_of_lifts")
    var numberOfLifts: Long? = null,
    var year: Int? = null,
)