package itmo.sleeter.infosys.mapper

import itmo.sleeter.infosys.dto.response.CoordinateResponse
import itmo.sleeter.infosys.model.Coordinate
import org.mapstruct.Mapper
import org.springframework.stereotype.Component

@Mapper
@Component
interface CoordinateMapper {
    fun coordinateToCoordinateResponse(coordinate: Coordinate?) : CoordinateResponse
    fun xAndYToCoordinate(x: Long, y: Double) : Coordinate
}