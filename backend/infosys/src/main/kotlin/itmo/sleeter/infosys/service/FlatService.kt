package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.request.FlatFilter
import itmo.sleeter.infosys.dto.request.UpdateFlatRequest
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
import itmo.sleeter.infosys.specification.FlatSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant

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
        val user = userService.getCurrentUser()
        return flatMapper.flatToFlatResponse(
                flat,
                coordinateService.coordinateToCoordinateResponse(flat.coordinates),
                houseService.houseToHouseResponse(flat.house),
                userService.userToUserResponse(flat.userCreate),
                userService.userToUserResponse(flat.userUpdate),
            flat.userCreate?.id == user.id
            )
    }

    fun getFlats(pageable: Pageable, filter: FlatFilter): Page<FlatResponse> {
        val specification = FlatSpecification(filter).toSpecification()
        val page = flatRepository.findAll(specification, pageable)
        val user = userService.getCurrentUser()
        return PageImpl(
            page.content.map {
                    flat -> flatMapper.flatToFlatResponse(
                flat,
                coordinateService.coordinateToCoordinateResponse(flat.coordinates),
                houseService.houseToHouseResponse(flat.house),
                userService.userToUserResponse(flat.userCreate),
                userService.userToUserResponse(flat.userUpdate),
                        flat.userCreate?.id == user.id
            )
            },
            pageable,
            page.totalElements
        )
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
                user,
                Instant.now(),
                Instant.now()
            ))
        return flatMapper.flatToFlatResponse(
            flat,
            coordinateService.coordinateToCoordinateResponse(coordinate),
            houseService.houseToHouseResponse(house),
            userService.userToUserResponse(user),
            userService.userToUserResponse(user),
            true
        )
    }

    fun deleteFlatById(id: Long) {
        flatRepository.deleteById(id)
    }
    fun updateFlat(id: Long, req: UpdateFlatRequest) {
        val flat = flatRepository.findFlatById(id).orElseThrow()
        flat.name = req.name
        flat.coordinates?.x = req.coordinate.x
        flat.coordinates?.y = req.coordinate.y
        flat.area = req.area
        flat.price = req.price
        flat.balcony = req.balcony
        flat.timeToMetroOnFoot = req.timeToMetroOnFoot
        flat.numberOfRooms = req.numberOfRooms
        flat.furnish = req.furnish
        flat.view = req.view
        flat.transport = req.transport
        flat.house = houseService.getHouse(req.houseId)
        flat.updatedAt = Instant.now()
        flatRepository.save(flat)
    }

    fun getFurnish() : FurnishResponse = FurnishResponse(Furnish.entries.map { f -> f.name })

    fun getTransport() : TransportResponse = TransportResponse(Transport.entries.map { t -> t.name })

    fun getView() : ViewResponse = ViewResponse(View.entries.map { v -> v.name })
}


