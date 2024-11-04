package itmo.sleeter.infosys.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "houses")
class House {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "houses_id_gen")
    @SequenceGenerator(name = "houses_id_gen", sequenceName = "houses_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null

    @NotNull
    @Column(name = "number_of_lifts", nullable = false)
    var numberOfLifts: Long? = null

    @NotNull
    @Column(name = "year", nullable = false)
    var year: Int? = null

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    var createdAt: Instant? = null

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_create_id")
    var userCreate: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_update_id")
    var userUpdate: User? = null

    @OneToMany(mappedBy = "house")
    var flats: MutableSet<Flat> = mutableSetOf()
}