package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.dto.request.CreateFlatRequest
import itmo.sleeter.infosys.dto.response.FlatResponse
import itmo.sleeter.infosys.dto.response.api.ApiResponse
import itmo.sleeter.infosys.service.FlatService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
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
    fun getFlat(@PathVariable id : Long) : ResponseEntity<FlatResponse> =
        ResponseEntity.ok(flatService.getFlatById(id))


    @PostMapping
    fun createFlat(@RequestBody req: CreateFlatRequest) : ResponseEntity<FlatResponse> {
        val flat = flatService.createFlat(req)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.LOCATION, "/api/flat/${flat.id}")
        return ResponseEntity.ok().headers(headers).body(flat)
    }
}