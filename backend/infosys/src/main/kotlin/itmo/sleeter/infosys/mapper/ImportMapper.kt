package itmo.sleeter.infosys.mapper

import itmo.sleeter.infosys.dto.response.ImportResponse
import itmo.sleeter.infosys.model.Import
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.stereotype.Component

@Mapper
@Component
interface ImportMapper {
    @Mapping(target = "userId", source = "userCreate.id")
    fun importToImportResponse(import: Import): ImportResponse
}