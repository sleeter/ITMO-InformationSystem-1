package itmo.sleeter.infosys.configuration.jwt

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    @NotBlank
    val secret: String,
    val exp: Long
)
