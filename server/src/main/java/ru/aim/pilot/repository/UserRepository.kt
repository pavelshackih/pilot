package ru.aim.pilot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import ru.aim.pilot.model.User

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
interface UserRepository : JpaRepository<User, Long> {

    fun findByName(@Param("name") name: String): List<User>
}
