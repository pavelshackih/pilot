package ru.aim.pilot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.aim.pilot.model.Revision
import ru.aim.pilot.model.RevisionType
import ru.aim.pilot.model.Territory
import ru.aim.pilot.repository.RevisionRepository
import ru.aim.pilot.repository.TerritoryRepository
import java.util.*

@Service
open class RevisionService
@Autowired constructor(private val territoryRepository: TerritoryRepository, private val revisionRepository: RevisionRepository) {

    fun findTerritory(id: Long?): Territory? = territoryRepository.findOne(id)

    fun findRevision(id: Long?): Revision? = revisionRepository.findOne(id)

    fun saveRevision(revision: Revision): Revision? {
        revision.lastUpdateDate = Date()
        return revisionRepository.save(revision)
    }

    fun findAllTerritories(): List<Territory> = territoryRepository.findAll()

    fun findByTerritoryIdAndType(id: Long?, type: RevisionType?): List<Revision> = revisionRepository.findByTerritoryIdAndType(id, type)

    fun deleteRevision(id: Long?) = revisionRepository.delete(id)
}