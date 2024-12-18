package itmo.sleeter.infosys.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "requests")
class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_id_gen")
    @SequenceGenerator(name = "requests_id_gen", sequenceName = "requests_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    var admin: User? = null

    @NotNull
    @Column(name = "approved", nullable = false)
    var approved: Boolean? = false
}