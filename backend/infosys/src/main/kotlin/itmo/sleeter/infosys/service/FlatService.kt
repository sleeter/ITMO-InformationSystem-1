package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.response.FlatResponse
import itmo.sleeter.infosys.dto.response.FurnishResponse
import itmo.sleeter.infosys.dto.response.TransportResponse
import itmo.sleeter.infosys.dto.response.ViewResponse
import itmo.sleeter.infosys.enumeration.Furnish
import itmo.sleeter.infosys.enumeration.Transport
import itmo.sleeter.infosys.enumeration.View
import itmo.sleeter.infosys.exception.EntityNotFoundException
import itmo.sleeter.infosys.mapper.FlatMapper
import itmo.sleeter.infosys.repository.FlatRepository
import org.springframework.stereotype.Service

@Service
class FlatService(
    private val flatRepository: FlatRepository,
    private val flatMapper: FlatMapper,
    private val coordinateService: CoordinateService,
    private val houseService: HouseService,
    private val userService: UserService
) {

    fun getFlatById(id : Long) : FlatResponse {
        val flat = flatRepository
            .findFlatById(id)
            .orElseThrow {
                EntityNotFoundException("Flat with id=$id not found")
            }
        return flatMapper.flatToFlatResponse(
                flat,
                coordinateService.coordinateToCoordinateResponse(flat.coordinates),
                houseService.houseToHouseResponse(flat.house),
                userService.userToUserResponse(flat.userCreate),
                userService.userToUserResponse(flat.userUpdate)
            )
    }
    fun getFlats() : List<FlatResponse> = flatRepository.findAll().map {
        flat -> flatMapper.flatToFlatResponse(
            flat,
            coordinateService.coordinateToCoordinateResponse(flat.coordinates),
            houseService.houseToHouseResponse(flat.house),
            userService.userToUserResponse(flat.userCreate),
            userService.userToUserResponse(flat.userUpdate)
        )
    }
    fun deleteAllByHouseId(houseId: Long) {
        flatRepository.deleteAllByHouseId(houseId)
    }
    fun createFlat(req: CreateFlatRequest) : FlatResponse {
        val coordinate = coordinateService.findCoordinateByXAndY(req.coordinate.x, req.coordinate.y)
        val house = houseService.getHouse(req.houseId)
        val user = userService.getUser(req.userId)
        val flat = flatRepository.save(
            flatMapper.createFlatRequestToFlat(
                req,
                coordinate,
                house,
                user,
                user
            ))
        return flatMapper.flatToFlatResponse(
            flat,
            coordinateService.coordinateToCoordinateResponse(coordinate),
            houseService.houseToHouseResponse(house),
            userService.userToUserResponse(user),
            userService.userToUserResponse(user)
        )
    }
    fun deleteFlatById(id: Long) {
        flatRepository.deleteById(id)
    }
    fun getFurnish() : FurnishResponse = FurnishResponse(Furnish.entries.map { f -> f.name })
    fun getTransport() : TransportResponse = TransportResponse(Transport.entries.map { t -> t.name })
    fun getView() : ViewResponse = ViewResponse(View.entries.map { v -> v.name })
}


