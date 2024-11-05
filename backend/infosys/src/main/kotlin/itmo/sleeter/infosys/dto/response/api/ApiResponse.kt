package itmo.sleeter.infosys.dto.response.api

data class ApiResponse<D>(
    var data: D? = null,
    val errors: MutableList<ApiError> = mutableListOf()
) {
    constructor(data: D) : this(data, mutableListOf())

    constructor(data: D, vararg apiErrors: ApiError) : this(data) {
        errors.addAll(apiErrors)
    }

    companion object {
        fun <D> success(data: D): ApiResponse<D> = ApiResponse(data)

        fun <D> success(): ApiResponse<D?> = ApiResponse(null)

        fun error(vararg errors: ApiError): ApiResponse<Nothing?> = ApiResponse(null, *errors)
    }

    fun addError(apiError: ApiError) {
        errors.add(apiError)
    }
}
