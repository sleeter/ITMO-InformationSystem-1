package itmo.sleeter.infosys.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "login", nullable = false, length = 50)
    var login: String? = null

    @Size(max = 100)
    @NotNull
    @Column(name = "password", nullable = false, length = 100)
    var password: String? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "role", nullable = false, length = 50)
    var role: String? = null

    @OneToMany(mappedBy = "userCreate")
    var flats: MutableSet<Flat> = mutableSetOf()

    @OneToMany(mappedBy = "userCreate")
    var houses: MutableSet<House> = mutableSetOf()
}