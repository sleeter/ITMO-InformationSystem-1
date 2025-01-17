package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.request.FlatFilter
import itmo.sleeter.infosys.dto.request.UpdateFlatRequest
import itmo.sleeter.infosys.dto.request.yaml.YamlData
import itmo.sleeter.infosys.dto.response.*
import itmo.sleeter.infosys.enumeration.Furnish
import itmo.sleeter.infosys.enumeration.Transport
import itmo.sleeter.infosys.enumeration.View
import itmo.sleeter.infosys.exception.EntityNotFoundException
import itmo.sleeter.infosys.mapper.FlatMapper
import itmo.sleeter.infosys.model.Flat
import itmo.sleeter.infosys.model.Import
import itmo.sleeter.infosys.repository.FlatRepository
import itmo.sleeter.infosys.specification.FlatSpecification
import jakarta.persistence.EntityExistsException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@Service
class FlatService(
    private val flatRepository: FlatRepository,
    private val flatMapper: FlatMapper,
    private val coordinateService: CoordinateService,
    private val houseService: HouseService,
    private val userService: UserService,
    private val minioService: MinioService
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
        val uniqueFlat = flatRepository.findFlatByName(req.name)
        if (uniqueFlat.isPresent) {
            throw EntityExistsException("Flat ${req.name} already exists")
        }
        val coordinate = coordinateService.findCoordinateByXAndY(req.coordinate.x, req.coordinate.y)
        val house = houseService.getHouse(req.houseId)
        val user = userService.getCurrentUser()
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

    // Удалить квартиры по транспорту
    fun deleteFlatsByTransport(transport: String) {
        flatRepository.deleteFlatsByTransport(transport)
    }

    // Подсчитать квартиры с house_id, у которых цена меньше заданного
    fun countFlatsByHouseIdLessThan(houseId: Long): Int {
        return flatRepository.findAll().count { flat -> flat.id!! < houseId };
    }


    // Получить более дешёвую квартиру
    fun getCheaperFlat(id1: Long, id2: Long): FlatResponse? {
        var flat1 = flatRepository.findFlatById(id1).orElseThrow()
        val flat2 = flatRepository.findFlatById(id2).orElseThrow()
        if(flat1.price!! > flat2.price!!) {
            flat1 = flat2
        }
        val user = userService.getCurrentUser()
        return flat1?.let {
            flatMapper.flatToFlatResponse(
                it, coordinateService.coordinateToCoordinateResponse(flat1.coordinates),
                houseService.houseToHouseResponse(flat1.house),
                userService.userToUserResponse(flat1.userCreate),
                userService.userToUserResponse(flat1.userUpdate),
                flat1.userCreate?.id == user.id)
        }
    }

    // Получить список квартир, отсортированных по времени до метро
    fun getFlatsSortedByMetroTime(): List<FlatResponse> {
        val flats = flatRepository.findAll().sortedBy { it.timeToMetroOnFoot }
        val user = userService.getCurrentUser()
        return flats.map {
                flat -> flatMapper.flatToFlatResponse(
            flat,
            coordinateService.coordinateToCoordinateResponse(flat.coordinates),
            houseService.houseToHouseResponse(flat.house),
            userService.userToUserResponse(flat.userCreate),
            userService.userToUserResponse(flat.userUpdate),
            flat.userCreate?.id == user.id
        )
        }
    }
    fun createFlatsFromFile(flats: List<itmo.sleeter.infosys.dto.request.yaml.Flat>, user: UserResponse): Int {
        val arr = mutableListOf<Flat>()
        val currentUser = userService.getUser(user.id)
        var count: Int = 0
        flats.forEach { flat ->
            val uniqueFlat = flatRepository.findFlatByName(flat.name)
            if (uniqueFlat.isPresent) {
                throw EntityExistsException("Flat ${flat.name} already exists")
            }
            val f = Flat()
            f.name = flat.name
            f.area = flat.area
            f.price = flat.price
            f.balcony = flat.balcony
            f.timeToMetroOnFoot = flat.timeToMetroOnFoot
            f.numberOfRooms = flat.numberOfRooms
            f.furnish = flat.furnish
            f.view = flat.view
            f.transport = flat.transport
            f.creationDate = Instant.now()
            f.updatedAt = Instant.now()
            f.userCreate = currentUser
            f.userUpdate = currentUser
            if (flat.coordinatesId != null) {
                f.coordinates = coordinateService.findCoordinateById(flat.coordinatesId)
            } else if (flat.coordinates != null) {
                f.coordinates = coordinateService.findCoordinateByXAndY(flat.coordinates.first().x, flat.coordinates.first().y)
                count++
            }
            if (flat.houseId != null) {
                f.house = houseService.getHouse(flat.houseId)
            } else if (flat.house != null) {
                val req = CreateHouseRequest(
                    flat.house.first().name,
                    flat.house.first().numberOfLifts,
                    flat.house.first().year,
                    user.id,
                )
                val h = houseService.createHouseWithUser(req, currentUser)
                f.house = houseService.getHouse(h.id)
                count++
            }
            arr.add(f)
        }
        flatRepository.saveAll(arr)
        return count + arr.size
    }
}


