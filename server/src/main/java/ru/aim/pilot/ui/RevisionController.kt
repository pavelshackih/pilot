package ru.aim.pilot.ui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import ru.aim.pilot.model.Revision
import ru.aim.pilot.model.RevisionType
import ru.aim.pilot.service.RevisionService
import ru.aim.pilot.spring.UiStringResolver
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/revision")
class RevisionController
@Autowired
constructor(val revisionService: RevisionService, val uiStringResolver: UiStringResolver) {

    @RequestMapping("/list", method = arrayOf(RequestMethod.GET))
    fun list(@RequestParam(value = "id", required = false) id: Long?, httpSession: HttpSession): ModelAndView {
        val territoryId = httpSession.getTerritoryId(id)
        val modelAndView = ModelAndView("list")
        val territory = revisionService.findTerritory(territoryId)
        modelAndView.addObject("territory", territory)

        RevisionType.values().forEach {
            val name = it.name.toLowerCase()
            val list = revisionService.findByTerritoryIdAndType(territory?.id, it)
            modelAndView.addObject(name, list)
            modelAndView.addObject("${name}CheckCount", list.fold(0, { i, revision -> i + revision.checkCount }))
            modelAndView.addObject("${name}AllViolationsCount", list.fold(0, { i, revision -> i + revision.allViolationsCount }))
            modelAndView.addObject("${name}FixedViolationsCount", list.fold(0, { i, revision -> i + revision.fixedViolationsCount }))
        }

        modelAndView.addObject("headers", uiStringResolver.resolveKeys(tableHeaders))
        return modelAndView
    }

    @RequestMapping("/delete", method = arrayOf(RequestMethod.GET))
    fun remove(@RequestParam("terId", required = false) terId: Long?, @RequestParam("revId") revId: Long?, @RequestParam("revType") revType: Int, httpSession: HttpSession): String {
        val territoryId = httpSession.getTerritoryId(terId)
        revisionService.deleteRevision(revId)
        val type = RevisionType.values()[revType]
        return "redirect:/revision/list?id=${territoryId.toString()}#${type.name.toLowerCase()}"
    }

    /**
     * Шапка таблицы, ссылки на названия полей Revision, по которым определяются строки в колонках через ресурсы
     */
    val tableHeaders = listOf(Revision::id.name, Revision::subjectName.name, Revision::address.name, Revision::inn.name,
            Revision::typeSafeSystem.name, Revision::checkCount.name, Revision::allViolationsCount.name,
            Revision::fixedViolationsCount.name, Revision::violationsDesc.name, Revision::violationsMark.name,
            Revision::lastUpdateDate.name, null)
}