package itmo.sleeter.infosys.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class FlatFilter(

    val name: String? = null,
    val area: Double? = null,
    val price: Double? = null,
    val balcony: Boolean? = null,
    @JsonProperty("time_to_metro_on_foot")
    var timeToMetroOnFoot : Double? = null,
    @JsonProperty("number_of_rooms")
    var numberOfRooms: Long? = null,
    val furnish: String? = null,
    val view: String? = null,
    val transport: String? = null
)

