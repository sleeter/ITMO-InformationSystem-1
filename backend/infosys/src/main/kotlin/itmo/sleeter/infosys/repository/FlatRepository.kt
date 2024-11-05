package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.Flat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface FlatRepository : JpaRepository<Flat, Long> {

//    @Query(
//        """
//            SELECT f from Flat f JOIN FETCH f.coordinates, f.house, f.userCreate, f.userUpdate WHERE
//            f.id = :id
//        """
//    )
//    fun findFlatByIdWithCoordinatesAndHouseAndUserCreateAndUserUpdateFetch(id: Long) : Optional<Flat>
    fun findFlatById(id: Long) : Optional<Flat>
    fun deleteAllByHouseId(id: Long)
    fun save(flat: Flat) : Flat
}