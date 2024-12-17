package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.dto.request.CreateHouseRequest
import itmo.sleeter.infosys.dto.request.HouseFilter
import itmo.sleeter.infosys.dto.request.UpdateFlatRequest
import itmo.sleeter.infosys.dto.request.UpdateHouseRequest
import itmo.sleeter.infosys.dto.response.HouseResponse
import itmo.sleeter.infosys.service.HouseService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/house")
class HouseController(private val houseService: HouseService) {
    @GetMapping("/{id}")
    fun getHouse(@PathVariable @Min(0) id: Long) : ResponseEntity<HouseResponse> =
        ResponseEntity.ok(houseService.getHouseById(id))

    @GetMapping
    fun getHouses(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(name = "sort_by", defaultValue = "id") sortBy: String,
        @RequestParam(name = "sort_order", defaultValue = "asc") sortOrder: String,
        @RequestParam(name = "number_of_lifts", required = false) numberOfLifts: Long?,
        houseFilter: HouseFilter
    ) : ResponseEntity<Page<HouseResponse>> {
        houseFilter.numberOfLifts = numberOfLifts
        val sortDirection = if (sortOrder == "desc") Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy))
        return ResponseEntity.ok(houseService.getHouses(pageable, houseFilter))
    }

    @PostMapping
    fun createHouse(@RequestBody @Valid req: CreateHouseRequest) : ResponseEntity<HouseResponse> {
        val house = houseService.createHouse(req)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.LOCATION, "/api/house/${house.id}")
        return ResponseEntity.ok().headers(headers).body(house)
    }
    @DeleteMapping("/{id}")
    @Transactional
    fun deleteHouse(@PathVariable @Min(0) id: Long) : ResponseEntity<Void> {
        houseService.deleteHouse(id)
        return ResponseEntity.ok(null)
    }
    @PutMapping("/{id}")
    @Transactional
    fun updateFlat(@PathVariable id: Long, @RequestBody @Valid req: UpdateHouseRequest) : ResponseEntity<Void> {
        houseService.updateHouse(id, req)
        return ResponseEntity.ok().body(null)
    }
}