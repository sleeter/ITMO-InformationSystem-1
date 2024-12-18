package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Request
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository : JpaRepository<Request, Long> {
}