package itmo.sleeter.infosys.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @NotEmpty
    val login: String,
    @NotBlank
    @Size(min = 7)
    val password: String,
    val role: String
)
