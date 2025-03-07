package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.request.FlatFilter
import itmo.sleeter.infosys.dto.request.UpdateFlatRequest
import itmo.sleeter.infosys.dto.response.FlatResponse
import itmo.sleeter.infosys.dto.response.FurnishResponse
import itmo.sleeter.infosys.dto.response.TransportResponse
import itmo.sleeter.infosys.dto.response.ViewResponse
import itmo.sleeter.infosys.service.FlatService
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/flat")
class FlatController(
    private val flatService: FlatService,
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {

    @GetMapping("/{id}")
    fun getFlat(@PathVariable @Min(0) id : Long) : ResponseEntity<FlatResponse> =
        ResponseEntity.ok(flatService.getFlatById(id))

    @GetMapping
    fun getFlats(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(name = "sort_by", defaultValue = "id") sortBy: String,
        @RequestParam(name = "sort_order", defaultValue = "asc") sortOrder: String,
        @RequestParam(name = "time_to_metro_on_foot", required = false) timeToMetroOnFoot: Double?,
        @RequestParam(name = "number_of_rooms", required = false) numberOfRooms: Long?,
        flatFilter: FlatFilter
    ) : ResponseEntity<Page<FlatResponse>> {
        flatFilter.numberOfRooms = numberOfRooms
        flatFilter.timeToMetroOnFoot = timeToMetroOnFoot
        val sortDirection = if (sortOrder == "desc") Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy))
        val flats = flatService.getFlats(pageable, flatFilter)
        return ResponseEntity.ok(flats)
    }

    @PostMapping
    @Transactional
    fun createFlat(@RequestBody @Valid req: CreateFlatRequest) : ResponseEntity<FlatResponse> {
        val flat = flatService.createFlat(req)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.LOCATION, "/api/flat/${flat.id}")
        simpMessagingTemplate.convertAndSend("/topic/app", "")
        return ResponseEntity.ok().headers(headers).body(flat)
    }

    @DeleteMapping("/{id}")
    @Transactional
    fun deleteFlat(@PathVariable @Min(0) id: Long) : ResponseEntity<Void> {
        flatService.deleteFlatById(id)
        return ResponseEntity.ok().body(null)
    }
    @PutMapping("/{id}")
    @Transactional
    fun updateFlat(@PathVariable id: Long, @RequestBody @Valid req: UpdateFlatRequest) : ResponseEntity<Void> {
        flatService.updateFlat(id, req)
        return ResponseEntity.ok().body(null)
    }
    @GetMapping("/furnish")
    fun getFurnish() : ResponseEntity<FurnishResponse> =
        ResponseEntity.ok(flatService.getFurnish())
    @GetMapping("/transport")
    fun getTransport() : ResponseEntity<TransportResponse> =
        ResponseEntity.ok(flatService.getTransport())
    @GetMapping("/view")
    fun getView() : ResponseEntity<ViewResponse> =
        ResponseEntity.ok(flatService.getView())

    @DeleteMapping("/func/delete/{transport}")
    @Transactional
    fun deleteFlatsByTransport(@PathVariable transport: String) {
        flatService.deleteFlatsByTransport(transport)
    }

    @GetMapping("/func/count/{houseId}")
    fun countFlatsByHouseIdLessThan(@PathVariable houseId: Long): Int {
        return flatService.countFlatsByHouseIdLessThan(houseId)
    }

    @GetMapping("/func/cheaper")
    fun getCheaperFlat(@RequestParam id1: Long, @RequestParam id2: Long): FlatResponse? {
        return flatService.getCheaperFlat(id1, id2)
    }

    @GetMapping("/func/sorted")
    fun getFlatsSortedByMetroTime(): List<FlatResponse> {
        return flatService.getFlatsSortedByMetroTime()
    }
}