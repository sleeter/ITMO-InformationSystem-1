package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.House
import org.springframework.data.jpa.repository.JpaRepository

interface HouseRepository : JpaRepository<House, Long> {
}