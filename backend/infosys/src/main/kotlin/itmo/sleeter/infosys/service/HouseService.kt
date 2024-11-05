package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.exception.HouseNotFoundException
import itmo.sleeter.infosys.exception.UserNotFoundException
import itmo.sleeter.infosys.mapper.HouseMapper
import itmo.sleeter.infosys.model.House
import itmo.sleeter.infosys.repository.HouseRepository
import org.springframework.stereotype.Service

@Service
class HouseService(
    private val houseRepository: HouseRepository,
    private val houseMapper: HouseMapper,
    private val userService: UserService,
    private val flatService: FlatService
) {
    fun houseToHouseResponse(house: House?) : HouseResponse = houseMapper.houseToHouseResponse(house)
    fun getHouse(id: Long) : House {
        val house = houseRepository
            .findHouseByIdWithUserCreateAndUserUpdateFetch(id)
            .orElseThrow {
                HouseNotFoundException("House with id=$id not found")
            }
        return house
    }
    fun getHouseResponse(id: Long) : HouseResponse = houseToHouseResponse(getHouse(id))
    fun createHouse(req: CreateHouseRequest) : HouseResponse {
        val user = userService
            .getUser(req.userId)
        return houseMapper.houseToHouseResponse(houseRepository.save(houseMapper.createHouseRequestToHouse(req, user, user)))
    }
    fun deleteHouse(id: Long) {
        flatService.deleteAllByHouseId(id)
        houseRepository.deleteById(id)
    }
}