package itmo.sleeter.infosys.kafka

import itmo.sleeter.infosys.dto.response.UserResponse

data class KafkaDTO(
    val file: ByteArray,
    val fileName: String,
    val importId: Long,
    val user: UserResponse,
)
