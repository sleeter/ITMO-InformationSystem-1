package itmo.sleeter.infosys.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "flats")
class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flats_id_gen")
    @SequenceGenerator(name = "flats_id_gen", sequenceName = "flats_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coordinates_id", nullable = false)
    var coordinates: Coordinate? = null

    @NotNull
    @Column(name = "area", nullable = false)
    var area: Double? = null

    @NotNull
    @Column(name = "price", nullable = false)
    var price: Double? = null

    @NotNull
    @Column(name = "balcony", nullable = false)
    var balcony: Boolean? = false

    @NotNull
    @Column(name = "time_to_metro_on_foot", nullable = false)
    var timeToMetroOnFoot: Double? = null

    @Column(name = "number_of_rooms")
    var numberOfRooms: Long? = null

    @Size(max = 50)
    @Column(name = "furnish", length = 50)
    var furnish: String? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "view", nullable = false, length = 50)
    var view: String? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "transport", nullable = false, length = 50)
    var transport: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    var house: House? = null

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "creation_date", nullable = false)
    var creationDate: Instant? = null

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_create_id")
    var userCreate: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_update_id")
    var userUpdate: User? = null
}