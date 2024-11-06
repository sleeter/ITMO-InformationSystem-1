package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.dto.request.LoginRequest
import itmo.sleeter.infosys.dto.request.RegisterRequest
import itmo.sleeter.infosys.dto.response.TokenResponse
import itmo.sleeter.infosys.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userService: UserService) {

    @PostMapping("/login")
    fun login(@RequestBody @Valid req: LoginRequest): ResponseEntity<TokenResponse> {
        val token = userService.login(req)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/register")
    fun register(@RequestBody @Valid req: RegisterRequest): ResponseEntity<TokenResponse> {
        val token = userService.register(req)
        return ResponseEntity.ok(token)
    }
}