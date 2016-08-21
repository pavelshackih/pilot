package ru.aim.pilot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.aim.pilot.model.Revision
import ru.aim.pilot.model.RevisionType

@Repository
interface RevisionRepository : JpaRepository<Revision, Long> {

    fun findByTerritoryIdAndType(id: Long?, type: RevisionType?): List<Revision>
}
