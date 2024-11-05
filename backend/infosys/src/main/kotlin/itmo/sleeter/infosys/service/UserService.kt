package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val UserRepository: UserRepository) {

}