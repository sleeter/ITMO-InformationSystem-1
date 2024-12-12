package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.exception.EntityNotFoundException
import itmo.sleeter.infosys.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String) : UserDetails = userRepository.findByLogin(username).orElseThrow {
        throw EntityNotFoundException("User with login=$username not found")
    }
}