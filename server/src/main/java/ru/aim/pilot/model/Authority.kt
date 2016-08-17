package ru.aim.pilot.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Authority(
        @Id
        @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
        var id: Long? = null,
        @OneToOne
        var user: User? = null,
        var authority: String? = null
)