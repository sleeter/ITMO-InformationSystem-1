package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Coordinate
import org.springframework.data.jpa.repository.JpaRepository

interface CoordinateRepository : JpaRepository<Coordinate, Long> {
}