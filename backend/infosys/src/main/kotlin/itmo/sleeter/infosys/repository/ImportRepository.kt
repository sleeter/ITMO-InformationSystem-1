package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Import
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImportRepository : JpaRepository<Import, Long> {
}