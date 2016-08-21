package ru.aim.pilot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.aim.pilot.model.Territory

@Repository
interface TerritoryRepository : JpaRepository<Territory, Long>
