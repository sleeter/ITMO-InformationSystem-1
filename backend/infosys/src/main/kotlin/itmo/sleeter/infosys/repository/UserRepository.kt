package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}