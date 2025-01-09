package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.service.FlatService
import itmo.sleeter.infosys.service.HouseService
import itmo.sleeter.infosys.service.ParseService
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/infosys/lab2")
class UtilHandler(
    private val parseService: ParseService,
    private val houseService: HouseService,
    private val flatService: FlatService
) {

    @PostMapping
    @Transactional
    fun parseFileAndInsert(@RequestParam("file") file: MultipartFile): ResponseEntity<Void> {
        val data = parseService.parseYaml(file)
        houseService.createHousesFromFile(data.house)
        flatService.createFlatsFromFile(data.flat)
        return ResponseEntity.noContent().build()
    }
}