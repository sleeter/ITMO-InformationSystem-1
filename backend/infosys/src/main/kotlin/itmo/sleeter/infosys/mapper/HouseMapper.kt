package itmo.sleeter.infosys.mapper

import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.model.House
import itmo.sleeter.infosys.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.stereotype.Component

@Mapper
@Component
interface HouseMapper {
    fun houseToHouseResponse(house: House?) : HouseResponse
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flats", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun createHouseRequestToHouse(
        createHouseRequest: CreateHouseRequest,
        userCreate: User,
        userUpdate: User
    ): House
}