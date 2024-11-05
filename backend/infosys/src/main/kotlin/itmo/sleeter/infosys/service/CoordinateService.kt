package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.repository.CoordinateRepository
import org.springframework.stereotype.Service

@Service
class CoordinateService(private val CoordinateRepository : CoordinateRepository) {
}