package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.exception.EntityNotFoundException
import itmo.sleeter.infosys.mapper.HouseMapper
import itmo.sleeter.infosys.model.House
import itmo.sleeter.infosys.repository.FlatRepository
import itmo.sleeter.infosys.repository.HouseRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class HouseService(
    private val houseRepository: HouseRepository,
    private val houseMapper: HouseMapper,
    private val userService: UserService,
    private val flatRepository: FlatRepository
) {
    fun houseToHouseResponse(house: House?) : HouseResponse = houseMapper.houseToHouseResponse(house)

    fun getHouse(id: Long) : House {
        val house = houseRepository
            .findHouseById(id)
            .orElseThrow {
                EntityNotFoundException("House with id=$id not found")
            }
        return house
    }

    fun getHouseById(id: Long) : HouseResponse {
        val house = houseRepository
            .findHouseById(id)
            .orElseThrow {
                EntityNotFoundException("House with id=$id not found")
            }
        return houseMapper.houseToHouseResponse(house)
    }

    fun getHouses() : List<HouseResponse> = houseRepository.findAll().map { h -> houseMapper.houseToHouseResponse(h) }

    fun createHouse(req: CreateHouseRequest) : HouseResponse {
        val user = userService.getUser(req.userId)
        return houseMapper.houseToHouseResponse(houseRepository.save(houseMapper.createHouseRequestToHouse(req, user, user, Instant.now(), Instant.now())))
    }

    fun deleteHouse(id: Long) {
        flatRepository.deleteAllByHouseId(id)
        houseRepository.deleteById(id)
    }
}