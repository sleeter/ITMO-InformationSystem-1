package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Flat
import org.springframework.data.jpa.repository.JpaRepository

interface FlatRepository : JpaRepository<Flat, Long> {
}