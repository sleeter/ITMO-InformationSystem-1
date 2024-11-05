package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Coordinate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CoordinateRepository : JpaRepository<Coordinate, Long> {
    fun findCoordinateByXAndY(x: Long, y: Double) : Optional<Coordinate>
}