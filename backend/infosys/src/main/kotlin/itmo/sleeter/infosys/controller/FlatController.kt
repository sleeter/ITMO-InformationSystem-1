package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.response.FlatResponse
import itmo.sleeter.infosys.dto.response.FurnishResponse
import itmo.sleeter.infosys.dto.response.TransportResponse
import itmo.sleeter.infosys.dto.response.ViewResponse
import itmo.sleeter.infosys.service.FlatService
import jakarta.transaction.Transactional
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
@RequestMapping("/api/flat")
class FlatController(private val flatService: FlatService) {

    @GetMapping("/{id}")
    fun getFlat(@PathVariable @Min(0) id : Long) : ResponseEntity<FlatResponse> =
        ResponseEntity.ok(flatService.getFlatById(id))

    @GetMapping
    fun getFlats() : ResponseEntity<List<FlatResponse>> =
        ResponseEntity.ok(flatService.getFlats())

    @PostMapping
    @Transactional
    fun createFlat(@RequestBody @Valid req: CreateFlatRequest) : ResponseEntity<FlatResponse> {
        val flat = flatService.createFlat(req)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.LOCATION, "/api/flat/${flat.id}")
        return ResponseEntity.ok().headers(headers).body(flat)
    }

    @DeleteMapping("/{id}")
    @Transactional
    fun deleteFlat(@PathVariable @Min(0) id: Long) : ResponseEntity<Void> {
        flatService.deleteFlatById(id)
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
}