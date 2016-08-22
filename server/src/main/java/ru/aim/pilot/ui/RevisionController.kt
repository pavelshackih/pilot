package ru.aim.pilot.ui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import ru.aim.pilot.model.Revision
import ru.aim.pilot.model.RevisionType
import ru.aim.pilot.service.RevisionService
import ru.aim.pilot.spring.UiStringResolver

@Controller
@RequestMapping("/revision")
class RevisionController
@Autowired
constructor(val revisionService: RevisionService, val uiStringResolver: UiStringResolver) {

    @RequestMapping("/list")
    internal fun list(@RequestParam("id") id: Long?): ModelAndView {
        val modelAndView = ModelAndView("list")
        val territory = revisionService.findTerritory(id)
        modelAndView.addObject("territory", territory)

        RevisionType.values().forEach {
            val name = it.name.toLowerCase()
            val list = revisionService.findByTerritoryIdAndType(territory?.id, it)
            modelAndView.addObject(name, list)
            modelAndView.addObject("${name}CheckCount", list.fold(0, { i, revision -> i + revision.checkCount }))
            modelAndView.addObject("${name}AllViolationsCount", list.fold(0, { i, revision -> i + revision.allViolationsCount }))
            modelAndView.addObject("${name}FixedViolationsCount", list.fold(0, { i, revision -> i + revision.fixedViolationsCount }))
        }

        modelAndView.addObject("headers", uiStringResolver.resolveFrom(tableHeaders))
        return modelAndView
    }

    @RequestMapping("/delete")
    internal fun remove(@RequestParam("terId") terId: Long?, @RequestParam("revId") revId: Long?, @RequestParam("revType") revType: Int): String {
        revisionService.deleteRevision(revId)
        val type = RevisionType.values()[revType]
        return "redirect:/revision/list?id=${terId.toString()}#${type.name.toLowerCase()}"
    }

    /**
     * Шапка таблицы, ссылки на поля Revision, по которым определяются строки в колонках через ресурсы
     */
    val tableHeaders = listOf(Revision::id, Revision::subjectName, Revision::address, Revision::inn,
            Revision::typeSafeSystem, Revision::checkCount, Revision::allViolationsCount,
            Revision::fixedViolationsCount, Revision::violationsDesc, Revision::violationsMark,
            Revision::lastUpdateDate, null)
}