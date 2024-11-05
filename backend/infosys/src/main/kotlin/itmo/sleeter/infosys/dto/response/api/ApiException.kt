package itmo.sleeter.infosys.dto.response.api

import lombok.Getter

@Getter
class ApiException(
    message: String,
    val errorCode: Int
) : RuntimeException(message)
