package ru.aim.pilot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.aim.pilot.model.User

@Repository
interface UserRepository : JpaRepository<User, Long>