package itmo.sleeter.infosys.service

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import itmo.sleeter.infosys.configuration.minio.MinioProperties
import itmo.sleeter.infosys.model.Import
import org.springframework.stereotype.Service

@Service
class MinioService(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties,
) {
    fun getFile(import: Import, filename: String): ByteArray {
        val user = import.userCreate!!
        val timestamp = import.createdAt!!
        val name = String.format("%s.%s.%s", user.id, timestamp.toString(), filename)
        val file = minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.bucket).`object`(name).build()).readAllBytes()
        return file
    }
    fun saveFile(import: Import, yamlFile: ByteArray, filename: String) {
        val user = import.userCreate!!
        val timestamp = import.createdAt!!
        val name = String.format("%s.%s.%s", user.id, timestamp.toString(), filename)
        val resp = minioClient.putObject(PutObjectArgs.builder().bucket(minioProperties.bucket).`object`(name).stream(yamlFile.inputStream(), -1, 10485760).build())
    }
}