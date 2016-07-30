package ru.aim.pilot.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Territory(
        @Id @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO) var id: Long? = null,
        var name: String? = null
)