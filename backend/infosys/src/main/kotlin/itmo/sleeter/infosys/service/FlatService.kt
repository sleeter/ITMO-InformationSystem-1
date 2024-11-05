package itmo.sleeter.infosys.service

import itmo.sleeter.infosys.repository.FlatRepository
import org.springframework.stereotype.Service

@Service
class FlatService(private val FlatRepository : FlatRepository) {
}