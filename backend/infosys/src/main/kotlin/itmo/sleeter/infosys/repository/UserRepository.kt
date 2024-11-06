package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): Optional<User>
    fun existsByLogin(login: String): Boolean
}