package itmo.sleeter.infosys.mapper

import itmo.sleeter.infosys.dto.response.UserResponse
import itmo.sleeter.infosys.model.User
import org.mapstruct.Mapper
import org.springframework.stereotype.Component

@Mapper
@Component
interface UserMapper {
    fun userToUserResponse(user: User?) : UserResponse
}