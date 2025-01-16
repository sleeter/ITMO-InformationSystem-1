package itmo.sleeter.infosys.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import itmo.sleeter.infosys.dto.request.yaml.YamlData
import itmo.sleeter.infosys.validator.Validator
import jakarta.validation.ValidationException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ParseService(
    private val validator: Validator,
) {

    fun parseYaml(yamlFile: ByteArray): YamlData {
        val mapper: ObjectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        val data: YamlData = mapper.readValue(yamlFile, YamlData::class.java)

        val errors = validator.validate(data)

        if (errors.isNotEmpty()) {
            throw ValidationException(errors.toString())
        }

        return data
    }
}