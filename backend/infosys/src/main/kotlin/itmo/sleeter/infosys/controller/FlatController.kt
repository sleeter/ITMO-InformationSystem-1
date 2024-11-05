package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.service.FlatService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/flat")
class FlatController(private val FlatService : FlatService) {
}