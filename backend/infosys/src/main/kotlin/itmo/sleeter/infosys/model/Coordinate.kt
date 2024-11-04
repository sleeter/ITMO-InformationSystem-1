package itmo.sleeter.infosys.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "coordinates")
class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinates_id_gen")
    @SequenceGenerator(name = "coordinates_id_gen", sequenceName = "coordinates_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @NotNull
    @Column(name = "x", nullable = false)
    var x: Long? = null

    @NotNull
    @Column(name = "y", nullable = false)
    var y: Double? = null

    @OneToMany(mappedBy = "coordinates")
    var flats: MutableSet<Flat> = mutableSetOf()
}