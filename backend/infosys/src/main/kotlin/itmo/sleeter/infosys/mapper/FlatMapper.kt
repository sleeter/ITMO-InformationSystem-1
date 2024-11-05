package itmo.sleeter.infosys.mapper

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.response.CoordinateResponse
import itmo.sleeter.infosys.dto.response.FlatResponse
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.dto.response.UserResponse
import itmo.sleeter.infosys.model.Coordinate
import itmo.sleeter.infosys.model.Flat
import itmo.sleeter.infosys.model.House
import itmo.sleeter.infosys.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.stereotype.Component

@Mapper
@Component
interface FlatMapper {
    @Mapping(target = "id", source = "flat.id")
    @Mapping(target = "updatedAt", source = "flat.updatedAt")
    @Mapping(target = "userCreate", source = "userCreate")
    @Mapping(target = "userUpdate", source = "userUpdate")
    @Mapping(target = "house.flats", ignore = true)
    fun flatToFlatResponse(
        flat: Flat,
        coordinate: CoordinateResponse,
        house: HouseResponse,
        userCreate: UserResponse,
        userUpdate: UserResponse
    ) : FlatResponse
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createFlatRequest.name")
    @Mapping(target = "house", source = "house")
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun createFlatRequestToFlat(
        createFlatRequest: CreateFlatRequest,
        coordinates: Coordinate,
        house: House,
        userCreate: User,
        userUpdate: User
    ) : Flat
}