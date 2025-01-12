package itmo.sleeter.infosys.configuration.minio

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "minio")
data class MinioProperties(
    val endpoint: String,
    val username: String,
    val password: String,
    val bucket: String,
)