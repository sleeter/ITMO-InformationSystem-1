package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.service.HouseService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/house")
class HouseController(private val houseService: HouseService) {
    @GetMapping("/{id}")
    fun getHouse(@PathVariable @Min(0) id: Long) : ResponseEntity<HouseResponse> =
        ResponseEntity.ok(houseService.getHouseResponse(id))

    @GetMapping
    fun getHouses() : ResponseEntity<List<HouseResponse>> =
        ResponseEntity.ok(houseService.getHouses())

    @PostMapping
    fun createHouse(@RequestBody @Valid req: CreateHouseRequest) : ResponseEntity<HouseResponse> {
        val house = houseService.createHouse(req)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.LOCATION, "/api/house/${house.id}")
        return ResponseEntity.ok().headers(headers).body(house)
    }
    @DeleteMapping("/{id}")
    fun deleteHouse(@PathVariable @Min(0) id: Long) : ResponseEntity<Void> {
        houseService.deleteHouse(id)
        return ResponseEntity.ok(null)
    }
}