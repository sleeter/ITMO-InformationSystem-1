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
import org.springframework.stereotype.Component

@Mapper
@Component
interface FlatMapper {
    fun flatToFlatResponse(
        flat: Flat,
        coordinate: CoordinateResponse,
        house: HouseResponse,
        userCreate: UserResponse,
        userUpdate: UserResponse
    ) : FlatResponse
    fun createFlatRequestToFlat(
        createFlatRequest: CreateFlatRequest,
        coordinate: Coordinate,
        house: House,
        userCreate: User,
        userUpdate: User
    ) : Flat
}