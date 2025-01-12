package itmo.sleeter.infosys.configuration.minio

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.minio.MinioClient

@Configuration
class MinioConfig(
    private val minioProperties: MinioProperties
) {

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(minioProperties.endpoint)
            .credentials(minioProperties.username, minioProperties.password)
            .build()
    }
}