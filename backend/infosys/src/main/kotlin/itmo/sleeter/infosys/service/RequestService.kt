package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.dto.request.AdminRequest
import itmo.sleeter.infosys.dto.response.AdminRequestResponse
import itmo.sleeter.infosys.enumeration.Role
import itmo.sleeter.infosys.model.Request
import itmo.sleeter.infosys.repository.RequestRepository
import itmo.sleeter.infosys.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class RequestService(
    private val requestRepository: RequestRepository,
    private val userService: UserService
) {
    fun saveRequest(req: AdminRequest) {
        val admin = userService.getCurrentUser()
        val user = userService.getUserByLogin(req.login)
        val request = Request()
        request.user = user
        request.admin = admin
        request.approved = false
        requestRepository.save(request)
    }
    fun getAllRequests(): List<AdminRequestResponse> {
        return requestRepository.findAll().filter { r -> r.approved == false }.map {
            r -> AdminRequestResponse(r.id, r.user?.login, r.admin?.login)
        }
    }
    fun approveRequest(id: Long) {
        val request = requestRepository.findById(id).orElseThrow()
        request.approved = true
        requestRepository.save(request)
        val user = userService.getUserByLogin(request.user?.login!!)
        user.role = Role.ROLE_ADMIN
        userService.userSave(user)
    }
}