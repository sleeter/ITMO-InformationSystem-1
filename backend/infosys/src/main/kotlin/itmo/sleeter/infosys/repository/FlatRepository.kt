package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Flat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface FlatRepository :
    PagingAndSortingRepository<Flat, Long>,
    JpaRepository<Flat, Long>,
    JpaSpecificationExecutor<Flat> {
    fun findFlatById(id: Long) : Optional<Flat>
    fun deleteAllByHouseId(id: Long)
    fun save(flat: Flat) : Flat
}