package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.response.ImportResponse
import itmo.sleeter.infosys.enumeration.Role
import itmo.sleeter.infosys.mapper.ImportMapper
import itmo.sleeter.infosys.model.Import
import itmo.sleeter.infosys.repository.ImportRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ImportService(
    private val importRepository: ImportRepository,
    private val importMapper: ImportMapper,
    private val userService: UserService
) {
    fun getImports(pageable: Pageable): Page<ImportResponse> {
        val page = importRepository.findAll(pageable).map { importMapper.importToImportResponse(it) }
        val user = userService.getCurrentUser()
        var content = page.content
        if (user.role!!.name != Role.ROLE_ADMIN.name) {
            content = page.filter { it.userId == user.id }.toList()
        }
        return PageImpl(
            content,
            pageable,
            page.totalElements,
        )
    }
    fun getImport(id: Long): Import {
        return importRepository.findById(id).get()
    }
    fun saveImport(filename: String): Import {
        val import = Import()
        import.count = 0
        import.userCreate = userService.getCurrentUser()
        import.status = "processing"
        import.createdAt = Instant.now()
        import.filename = filename
        return importRepository.save(import)
    }
    fun updateImport(id: Long, count: Int, status: String) {
        val import = importRepository.findById(id).get()
        import.count = count
        import.status = status
        importRepository.save(import)
    }
}