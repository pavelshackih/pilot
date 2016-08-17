package ru.aim.pilot.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class User(
        @Id @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO) var id: Long? = null,
        var name: String? = null,
        var password: String? = null,
        var enabled: Boolean = true,
        @OneToOne
        var territory: Territory? = null
)
