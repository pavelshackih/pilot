package ru.aim.pilot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import ru.aim.pilot.model.Revision

@Component
interface RevisionRepository : JpaRepository<Revision, Long>