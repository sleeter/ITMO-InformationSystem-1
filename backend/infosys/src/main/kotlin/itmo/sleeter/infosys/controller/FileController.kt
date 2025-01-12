package itmo.sleeter.infosys.controller

import itmo.sleeter.infosys.service.ImportService
import itmo.sleeter.infosys.service.MinioService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/file")
class FileController(
    private val minioService: MinioService,
    private val importService: ImportService
) {
    @GetMapping
    fun getFile(@RequestParam import: Long, @RequestParam filename: String): ResponseEntity<ByteArray> {
        val i = importService.getImport(import)
        val file = minioService.getFile(i, filename)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
        return ResponseEntity(file, headers, HttpStatus.OK)
    }
}