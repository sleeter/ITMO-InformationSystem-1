package itmo.sleeter.infosys.specification

import itmo.sleeter.infosys.dto.request.HouseFilter
import itmo.sleeter.infosys.model.House
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

class HouseSpecification(private val filter: HouseFilter) {

    fun toSpecification(): Specification<House> {
        return Specification { root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            filter.name?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get<String>("name")), "%${it.toLowerCase()}%"))
            }
            filter.numberOfLifts?.let {
                predicates.add(criteriaBuilder.equal(root.get<String>("numberOfLifts"), it))
            }
            filter.year?.let {
                predicates.add(criteriaBuilder.equal(root.get<String>("year"), it))
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}