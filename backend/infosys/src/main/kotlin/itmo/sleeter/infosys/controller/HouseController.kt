package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.service.HouseService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/house")
class HouseController(private val HouseService : HouseService) {
}