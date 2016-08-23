package ru.aim.pilot.ui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import ru.aim.pilot.service.RevisionService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Controller
class HomeController
@Autowired
constructor(private val revisionService: RevisionService) {

    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(model: Model, httpSession: HttpSession): String {
        val territoryId = httpSession.getTerritoryId(null)
        /**
         * Если в сессии пользователя не проставлена ссылка на управление
         * значит это админ и отображаем список всех управлений
         * иначе сразу переводим на страницу отделения
         */
        if (territoryId == null) {
            model.addAttribute("territories", revisionService.findAllTerritories())
            return "index"
        } else {
            return "redirect:/revision/list"
        }
    }

    @RequestMapping(value = "/logout", method = arrayOf(RequestMethod.GET))
    fun logout(request: HttpServletRequest, response: HttpServletResponse): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return "redirect:/"
    }
}