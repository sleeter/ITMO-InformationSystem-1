package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.LoginRequest
import itmo.sleeter.infosys.dto.request.RegisterRequest
import itmo.sleeter.infosys.dto.response.TokenResponse
import itmo.sleeter.infosys.dto.response.UserResponse
import itmo.sleeter.infosys.enumeration.Role
import itmo.sleeter.infosys.exception.EntityAlreadyExists
import itmo.sleeter.infosys.exception.EntityNotFoundException
import itmo.sleeter.infosys.mapper.UserMapper
import itmo.sleeter.infosys.model.User
import itmo.sleeter.infosys.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {
    fun userToUserResponse(user: User?) : UserResponse = userMapper.userToUserResponse(user)
    fun getUser(id: Long) : User {
        val user = userRepository.findById(id).orElseThrow {
            EntityNotFoundException("User with id=$id not found")
        }
        return user
    }
    fun register(req: RegisterRequest) : TokenResponse {
        val user = User()
        user.login = req.login
        user.setPassword(passwordEncoder.encode(req.password))
        user.role = if (req.role == "user") Role.ROLE_USER else Role.ROLE_ADMIN
        createUser(user)
        val jwt = jwtService.generateToken(user)
        return TokenResponse(jwt)
    }
    fun login(req: LoginRequest) : TokenResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
            req.login,
            req.password
        ))
        val user = getUserByLogin(req.login)
        val jwt = jwtService.generateToken(user)
        return TokenResponse(jwt)
    }

    fun userSave(user: User) : User = userRepository.save(user)
    fun createUser(user: User) : User {
        if (userRepository.existsByLogin(user.login!!)) {
            throw EntityAlreadyExists("User with login=${user.login} already exists")
        }
        return userSave(user)
    }
    fun getUserByLogin(login: String) : User = userRepository.findByLogin(login).orElseThrow {
        throw EntityNotFoundException("User with login=$login not found")
    }
    fun getCurrentUser(): User {
        val username = SecurityContextHolder.getContext().authentication.name
        return getUserByLogin(username)
    }
}