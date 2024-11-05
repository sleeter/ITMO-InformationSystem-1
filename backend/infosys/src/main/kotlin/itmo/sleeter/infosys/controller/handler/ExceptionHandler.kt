package itmo.sleeter.infosys.controller.handler

import itmo.sleeter.infosys.dto.response.api.ApiError
import itmo.sleeter.infosys.dto.response.api.ApiResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    private val log: Logger = LogManager.getLogger(ExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception) : ResponseEntity<ApiResponse<*>> {
        val err = ApiError(exception.message ?: "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR.value())
        log.error("Internal server error", exception)
        return ResponseEntity
            .internalServerError()
            .body(ApiResponse.error(err))
    }

}