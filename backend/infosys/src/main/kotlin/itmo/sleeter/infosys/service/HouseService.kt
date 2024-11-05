package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.repository.HouseRepository
import org.springframework.stereotype.Service

@Service
class HouseService(private val HouseRepository : HouseRepository) {
}