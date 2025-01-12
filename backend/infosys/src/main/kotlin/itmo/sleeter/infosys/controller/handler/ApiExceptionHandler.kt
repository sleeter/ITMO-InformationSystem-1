package itmo.sleeter.infosys.controller.handler

import jakarta.persistence.EntityExistsException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiExceptionHandler {
    private val log: Logger = LogManager.getLogger(ApiExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception) : ResponseEntity<*> {
        log.error("Internal server error", exception)
        return ResponseEntity
            .internalServerError()
            .body(exception.message)
    }
    @ExceptionHandler(EntityExistsException::class)
    fun handleException(exception: EntityExistsException) : ResponseEntity<*> {
        log.error("Entity already exists", exception)
        return ResponseEntity
            .badRequest()
            .body(exception.message)
    }
}