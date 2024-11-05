package itmo.sleeter.infosys.dto.response.api

data class ApiError(
    val message: String,
    val code: Int
) {
    constructor(exception: ApiException) : this(
        message = exception.message ?: "Unknown error",
        code = exception.errorCode
    )
}

