package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.response.FlatResponse
import itmo.sleeter.infosys.exception.FlatNotFoundException
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
            .findFlatByIdWithCoordinatesAndHouseAndUserCreateAndUserUpdateFetch(id)
            .orElseThrow {
                FlatNotFoundException("Flat with id=$id not found")
            }
        return flatMapper.flatToFlatResponse(
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
}


