package itmo.sleeter.infosys.specification

import itmo.sleeter.infosys.dto.request.FlatFilter
import itmo.sleeter.infosys.model.Flat
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

class FlatSpecification(private val filter: FlatFilter) {

    fun toSpecification(): Specification<Flat> {
        return Specification { root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            filter.name?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get<String>("name")), "%${it.toLowerCase()}%"))
            }

            filter.price?.let {
                predicates.add(criteriaBuilder.equal(root.get<Double>("price"), it))
            }

            filter.area?.let {
                predicates.add(criteriaBuilder.equal(root.get<Double>("area"), it))
            }

            filter.balcony?.let {
                predicates.add(criteriaBuilder.equal(root.get<Boolean>("balcony"), it))
            }

            filter.timeToMetroOnFoot?.let {
                predicates.add(criteriaBuilder.equal(root.get<Double>("timeToMetroOnFoot"), it))
            }

            filter.numberOfRooms?.let {
                predicates.add(criteriaBuilder.equal(root.get<Long>("numberOfRooms"), it))
            }

            filter.furnish?.let {
                predicates.add(criteriaBuilder.equal(root.get<String>("furnish"), it))
            }

            filter.view?.let {
                predicates.add(criteriaBuilder.equal(root.get<String>("view"), it))
            }

            filter.transport?.let {
                predicates.add(criteriaBuilder.equal(root.get<String>("transport"), it))
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}
