package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.dto.response.ImportResponse
import itmo.sleeter.infosys.kafka.KafkaMessagingService
import itmo.sleeter.infosys.service.FlatService
import itmo.sleeter.infosys.service.ImportService
import itmo.sleeter.infosys.service.ParseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/infosys/lab2")
class ImportController(
    private val importService: ImportService,
    private val kafkaMessagingService: KafkaMessagingService,
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {

    @PostMapping
    fun parseFileAndInsert(@RequestParam("file") file: MultipartFile): ResponseEntity<Void> {
        val import = file.originalFilename?.let { importService.saveImport(it) }
        import?.id?.let { kafkaMessagingService.sendFile(file, it) }
        simpMessagingTemplate.convertAndSend("/topic/app", "")
        return ResponseEntity.noContent().build()
    }
    @GetMapping
    fun getImports(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<Page<ImportResponse>> {
        val pageable: Pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(importService.getImports(pageable))
    }
}