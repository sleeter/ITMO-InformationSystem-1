package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Coordinate
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CoordinateRepository : JpaRepository<Coordinate, Long> {
    fun findCoordinateByXAndY(x: Long, y: Double) : Optional<Coordinate>
}