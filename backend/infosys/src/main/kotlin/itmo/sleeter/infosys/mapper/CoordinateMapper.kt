package itmo.sleeter.infosys.mapper

import itmo.sleeter.infosys.dto.response.CoordinateResponse
import itmo.sleeter.infosys.model.Coordinate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.stereotype.Component

@Mapper
@Component
interface CoordinateMapper {
    fun coordinateToCoordinateResponse(coordinate: Coordinate?) : CoordinateResponse
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flats", ignore = true)
    fun xAndYToCoordinate(x: Long, y: Double) : Coordinate
}