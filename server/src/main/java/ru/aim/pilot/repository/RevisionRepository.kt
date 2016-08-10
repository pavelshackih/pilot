package ru.aim.pilot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import ru.aim.pilot.model.Revision
import ru.aim.pilot.model.RevisionType

@Component
interface RevisionRepository : JpaRepository<Revision, Long> {

    fun findByTerritoryIdAndType(id: Long?, type: RevisionType?): List<Revision>

    fun findByType(type: RevisionType?): List<Revision>
}
