package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userService: UserService) {

    @PostMapping("/login")
    fun login() {
        return
    }

    @PostMapping("/register")
    fun register() {
        return
    }
}