package itmo.sleeter.infosys.mapper

import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.response.FlatResponse
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.model.House
import itmo.sleeter.infosys.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.stereotype.Component
import java.time.Instant

@Mapper
@Component
interface HouseMapper {
    @Mapping(target = "isMine", source = "isMine")
    fun houseToHouseResponse(house: House?, isMine : Boolean) : HouseResponse

    @Mapping(target = "flats", source = "flats")
    @Mapping(target = "isMine", ignore = true)
    fun houseToHouseResponseWithoutFlats(house: House?, flats: Set<FlatResponse>?) : HouseResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flats", ignore = true)
    fun createHouseRequestToHouse(
        createHouseRequest: CreateHouseRequest,
        userCreate: User,
        userUpdate: User,
        createdAt: Instant,
        updatedAt: Instant
    ): House
}