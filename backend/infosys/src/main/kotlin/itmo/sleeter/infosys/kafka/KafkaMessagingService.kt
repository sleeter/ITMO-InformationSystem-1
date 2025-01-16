package itmo.sleeter.infosys.kafka

import itmo.sleeter.infosys.mapper.UserMapper
import itmo.sleeter.infosys.service.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class KafkaMessagingService(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val houseService: HouseService,
    private val flatService: FlatService,
    private val minioService: MinioService,
    private val importService: ImportService,
    private val parseService: ParseService,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val userService: UserService,
    private val userMapper: UserMapper,
) {

    @Value("\${topic.send-file}")
    private val topic: String? = null

    fun sendFile(file: MultipartFile, importId: Long) {
        val user = userMapper.userToUserResponse(userService.getCurrentUser())
        if (topic != null) {
            file.originalFilename?.let { kafkaTemplate.send(topic, it, KafkaDTO(file.bytes, file.originalFilename!!, importId, user)) }
        }
    }
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @KafkaListener(
        topics = ["\${topic.send-file}"],
        groupId = "\${spring.kafka.consumer.group-id}",
        properties = arrayOf("spring.json.value.default.type=itmo.sleeter.infosys.kafka.KafkaDTO")
    )
    fun parseAndSendFile(dto: KafkaDTO) {
        val importId = dto.importId
        val import = importService.getImport(importId)

        val file = dto.file
        var count = 0
        try {
            val data = parseService.parseYaml(file)

            val count1 = houseService.createHousesFromFile(data.house, dto.user)
            val count2 = flatService.createFlatsFromFile(data.flat, dto.user)
            count = count1 + count2

            minioService.saveFile(import, file, dto.fileName)
        } catch (e: Exception) {
            import.id?.let { importService.updateImport(it, count, "rejected") }
            throw RuntimeException("Error with save file", e)
        }

        if (import != null && count != null) {
            import.id?.let { importService.updateImport(it, count, "accepted") }
        }
        simpMessagingTemplate.convertAndSend("/topic/app", "")
    }
}