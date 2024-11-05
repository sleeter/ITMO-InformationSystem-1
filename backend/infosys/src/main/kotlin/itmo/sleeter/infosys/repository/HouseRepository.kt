package itmo.sleeter.infosys.repository

import itmo.sleeter.infosys.model.House
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface HouseRepository : JpaRepository<House, Long> {
//    @Query(
//        """
//            SELECT h from House h JOIN FETCH h.userCreate, h.userUpdate WHERE h.id = :id
//        """
//    )
//    fun findHouseByIdWithUserCreateAndUserUpdateFetch(id: Long) : House
    fun findHouseById(id: Long) : Optional<House>

    fun save(house: House) : House
}