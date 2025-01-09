package itmo.sleeter.infosys.dto.request.yaml

import jakarta.validation.Valid

data class YamlData(
    @Valid
    val house: List<House>,
    @Valid
    val flat: List<Flat>
)
