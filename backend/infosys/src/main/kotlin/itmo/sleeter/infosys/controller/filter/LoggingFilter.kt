package itmo.sleeter.infosys.controller.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Component
class LoggingFilter : OncePerRequestFilter() {
    private val log: Logger = LogManager.getLogger(LoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.info("Received request: {}", request.requestURI)
        filterChain.doFilter(request, response)
        log.info("Processed request: {}", request.requestURI)
    }
}