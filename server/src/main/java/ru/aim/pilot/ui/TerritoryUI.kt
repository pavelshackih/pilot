package ru.aim.pilot.ui

import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.server.ExternalResource
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Link
import com.vaadin.ui.UI
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.viritin.layouts.MVerticalLayout
import ru.aim.pilot.repository.TerritoryRepository

@Title("Список территорий")
@SpringUI(path = "/territories")
@Theme("valo")
class TerritoryUI
@Autowired
constructor(private val territoryRepository: TerritoryRepository) : UI() {

    override fun init(request: VaadinRequest) {
        val links = territoryRepository.findAll().map { Link(it.name, ExternalResource("/revision?id=${it.id}")) }
        val verticalLayout = MVerticalLayout()
        links.forEach { verticalLayout.add(it) }
        content = verticalLayout
    }
}
