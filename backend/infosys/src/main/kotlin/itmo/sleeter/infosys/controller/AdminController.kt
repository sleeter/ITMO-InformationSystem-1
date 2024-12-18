package itmo.sleeter.infosys.controller

import com.fasterxml.jackson.annotation.JsonProperty
import itmo.sleeter.infosys.dto.request.AdminRequest
import itmo.sleeter.infosys.dto.response.AdminRequestResponse
import itmo.sleeter.infosys.service.RequestService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val requestService: RequestService,
) {

    @PostMapping("/request")
    fun request(@RequestBody req: AdminRequest) {
        requestService.saveRequest(req)
    }

    @GetMapping("/request")
    fun request() : ResponseEntity<List<AdminRequestResponse>> {
        return ResponseEntity.ok(requestService.getAllRequests())
    }

    @PostMapping("/approve/{id}")
    @Transactional
    fun approve(@PathVariable id: Long) {
        requestService.approveRequest(id)
    }
}