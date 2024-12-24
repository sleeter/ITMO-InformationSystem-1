package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Flat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
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
    @Query(value = "SELECT delete_flats_by_transport(:transport)", nativeQuery = true)
    fun deleteFlatsByTransport(transport: String)
    @Query(value = "SELECT count(*) FROM count_flats_by_house_id_less_than(:houseId)", nativeQuery = true)
    fun countFlatsByHouseIdLessThan(houseId: Long): Long
    @Query(value = "SELECT * FROM get_cheaper_flat(:id1, :id2)", nativeQuery = true)
    fun getCheaperFlat(id1: Long, id2: Long): Flat?
    @Query(value = "SELECT * FROM get_flats_sorted_by_metro_time()", nativeQuery = true)
    fun getFlatsSortedByMetroTime(): List<Flat>
}