package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.request.HouseFilter
import itmo.sleeter.infosys.dto.request.UpdateHouseRequest
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.dto.response.UserResponse
import itmo.sleeter.infosys.exception.EntityNotFoundException
import itmo.sleeter.infosys.mapper.HouseMapper
import itmo.sleeter.infosys.model.House
import itmo.sleeter.infosys.model.User
import itmo.sleeter.infosys.repository.FlatRepository
import itmo.sleeter.infosys.repository.HouseRepository
import itmo.sleeter.infosys.specification.HouseSpecification
import jakarta.persistence.EntityExistsException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class HouseService(
    private val houseRepository: HouseRepository,
    private val houseMapper: HouseMapper,
    private val userService: UserService,
    private val flatRepository: FlatRepository,
) {
    fun houseToHouseResponse(house: House?) : HouseResponse = houseMapper.houseToHouseResponse(house, false)

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
        val user = userService.getCurrentUser()
        val isMine = user.id == id
        return houseMapper.houseToHouseResponse(house, isMine)
    }

    fun getHouses(pageable: Pageable, filter: HouseFilter) : Page<HouseResponse> {
        val specification = HouseSpecification(filter).toSpecification()
        val page = houseRepository.findAll(specification, pageable)
        val user = userService.getCurrentUser()
        return PageImpl(
            page.content.map {
                    h ->
                houseMapper.houseToHouseResponse(h, h.userCreate?.id == user.id)
            },
            pageable,
            page.totalElements
        )
    }

    fun createHouse(req: CreateHouseRequest) : HouseResponse {
        val user = userService.getCurrentUser()
        val uniqueHouse = houseRepository.findHouseByName(req.name)
        if (uniqueHouse.isPresent) {
            throw EntityExistsException("House ${req.name} already exists")
        }
        return houseMapper.houseToHouseResponse(houseRepository.save(houseMapper.createHouseRequestToHouse(req, user, user, Instant.now(), Instant.now())), true)
    }
    fun createHouseWithUser(req: CreateHouseRequest, user: User) : HouseResponse {
        val uniqueHouse = houseRepository.findHouseByName(req.name)
        if (uniqueHouse.isPresent) {
            throw EntityExistsException("House ${req.name} already exists")
        }
        return houseMapper.houseToHouseResponse(houseRepository.save(houseMapper.createHouseRequestToHouse(req, user, user, Instant.now(), Instant.now())), true)
    }

    fun deleteHouse(id: Long) {
        flatRepository.deleteAllByHouseId(id)
        houseRepository.deleteById(id)
    }

    fun updateHouse(id: Long, req: UpdateHouseRequest) {
        val house = houseRepository.findHouseById(id).orElseThrow()
        house.name = req.name
        house.numberOfLifts = req.numberOfLifts
        house.year = req.year
        house.updatedAt = Instant.now()
        houseRepository.save(house)
    }

    fun createHousesFromFile(houses: List<itmo.sleeter.infosys.dto.request.yaml.House>, user: UserResponse): Int {
        val arr = mutableListOf<House>()
        val currentUser = userService.getUser(user.id)
        houses.forEach { house ->
            val uniqueHouse = houseRepository.findHouseByName(house.name)
            if (uniqueHouse.isPresent) {
                throw EntityExistsException("House ${house.name} already exists")
            }
            val h = House()
            h.name = house.name
            h.numberOfLifts = house.numberOfLifts
            h.year = house.year
            h.createdAt = Instant.now()
            h.updatedAt = Instant.now()
            h.userCreate = currentUser
            h.userUpdate = currentUser
            arr.add(h)
        }
        houseRepository.saveAll(arr)
        return arr.size
    }
}