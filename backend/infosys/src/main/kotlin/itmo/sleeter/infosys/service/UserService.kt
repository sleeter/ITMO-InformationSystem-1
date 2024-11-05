package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.response.UserResponse
import itmo.sleeter.infosys.exception.UserNotFoundException
import itmo.sleeter.infosys.mapper.UserMapper
import itmo.sleeter.infosys.model.User
import itmo.sleeter.infosys.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {
    fun userToUserResponse(user: User?) : UserResponse = userMapper.userToUserResponse(user)
    fun getUser(id: Long) : User {
        val user = userRepository.findById(id).orElseThrow {
            UserNotFoundException("User with id=$id not found")
        }
        return user
    }
}