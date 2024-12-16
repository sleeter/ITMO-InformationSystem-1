package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.House
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface HouseRepository : JpaRepository<House, Long> {
    fun findHouseById(id: Long) : Optional<House>

    fun save(house: House) : House
}