package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.response.CoordinateResponse
import itmo.sleeter.infosys.mapper.CoordinateMapper
import itmo.sleeter.infosys.model.Coordinate
import itmo.sleeter.infosys.repository.CoordinateRepository
import org.springframework.stereotype.Service

@Service
class CoordinateService(
    private val coordinateRepository: CoordinateRepository,
    private val coordinateMapper: CoordinateMapper
) {
    fun coordinateToCoordinateResponse(coordinate: Coordinate?) : CoordinateResponse = coordinateMapper.coordinateToCoordinateResponse(coordinate)
    fun findCoordinateByXAndY(x: Long, y: Double) : Coordinate {
        val coordinate = coordinateRepository
            .findCoordinateByXAndY(x, y)
            .orElse(coordinateMapper.xAndYToCoordinate(x, y))
        coordinateRepository.save(coordinate)
        return coordinate
    }
}