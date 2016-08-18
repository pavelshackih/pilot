package ru.aim.pilot.ui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import ru.aim.pilot.model.Revision
import ru.aim.pilot.model.RevisionType
import ru.aim.pilot.repository.RevisionRepository
import ru.aim.pilot.repository.TerritoryRepository

@Controller
@RequestMapping("/revision")
class RevisionController
@Autowired
constructor(private val territoryRepository: TerritoryRepository, private val revisionRepository: RevisionRepository) {

    @RequestMapping("/list")
    internal fun list(@RequestParam("id") id: Long?): ModelAndView {
        val modelAndView = ModelAndView("list")
        val territory = territoryRepository.findOne(id)
        modelAndView.addObject("territory", territory)

        val opoList = revisionRepository
                .findByTerritoryIdAndType(territory.id, RevisionType.OPO)
                .mapIndexed { i, revision -> revision.apply { order = i + 1 } }
        modelAndView.addObject("opo", opoList)

        modelAndView.addObject("opoCheckCount", opoList.fold(0, { i, revision -> i + revision.checkCount }))
        modelAndView.addObject("opoAllViolationsCount", opoList.fold(0, { i, revision -> i + revision.allViolationsCount }))
        modelAndView.addObject("opoFixedViolationsCount", opoList.fold(0, { i, revision -> i + revision.fixedViolationsCount }))

        val gtsList = revisionRepository
                .findByTerritoryIdAndType(territory.id, RevisionType.GTS)
                .mapIndexed { i, revision -> revision.apply { order = i + 1 } }
        modelAndView.addObject("gts", gtsList)

        modelAndView.addObject("gtsCheckCount", gtsList.fold(0, { i, revision -> i + revision.checkCount }))
        modelAndView.addObject("gtsAllViolationsCount", gtsList.fold(0, { i, revision -> i + revision.allViolationsCount }))
        modelAndView.addObject("gtsFixedViolationsCount", gtsList.fold(0, { i, revision -> i + revision.fixedViolationsCount }))

        modelAndView.addObject("headers", Revision.headers)
        return modelAndView
    }

    @RequestMapping("/delete")
    internal fun remove(@RequestParam("terId") terId: Long?, @RequestParam("revId") revId: Long?): String {
        revisionRepository.delete(revId)
        return "redirect:/revision/list?id=" + java.lang.Long.toString(terId!!)
    }
}