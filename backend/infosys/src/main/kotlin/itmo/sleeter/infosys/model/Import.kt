package itmo.sleeter.infosys.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "imports")
class Import {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imports_id_gen")
    @SequenceGenerator(name = "imports_id_gen", sequenceName = "imports_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @NotNull
    @Column(name = "status", nullable = false)
    var status: Boolean? = false

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_create_id")
    var userCreate: User? = null

    @NotNull
    @Column(name = "count", nullable = false)
    var count: Int? = null

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    var createdAt: Instant? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "filename", nullable = false, length = 50)
    var filename: String? = null
}