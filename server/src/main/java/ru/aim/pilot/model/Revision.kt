package ru.aim.pilot.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Revision(
        @Id @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
        var id: Long? = null,
        var subjectName: String? = null,
        var address: String? = null,
        var inn: Long = 0,
        var typeSafeSystem: String? = null,
        var checkCount: Int = 0,
        var allViolationsCount: Int = 0,
        var fixedViolationsCount: Int = 0,
        var violationsDesc: String? = null,
        var violationsMark: String? = null,
        var type: RevisionType = RevisionType.OPO,
        @ManyToOne
        var territory: Territory? = null,
        var lastUpdateDate: Date? = null
)