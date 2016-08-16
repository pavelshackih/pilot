package ru.aim.pilot.ui;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.vaadin.viritin.ListContainer;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.model.RevisionType;
import ru.aim.pilot.model.Territory;
import ru.aim.pilot.repository.RevisionRepository;
import ru.aim.pilot.repository.TerritoryRepository;
import ru.aim.pilot.ui.export.ExcelExportService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Controller
public class HomeController {

    private final TerritoryRepository territoryRepository;

    private final RevisionRepository revisionRepository;

    private final ExcelExportService excelExportService;

    @Autowired
    public HomeController(RevisionRepository revisionRepository, TerritoryRepository territoryRepository, ExcelExportService excelExportService) {
        this.revisionRepository = revisionRepository;
        this.territoryRepository = territoryRepository;
        this.excelExportService = excelExportService;
    }

    @RequestMapping("/")
    String index(Model model) {
        model.addAttribute("territories", territoryRepository.findAll());
        return "index";
    }


    @RequestMapping("/rev")
    ModelAndView revision(@RequestParam("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("revision");
        Territory territory = territoryRepository.findOne(id);
        modelAndView.addObject("territory", territory);
        modelAndView.addObject("opo", revisionRepository.findByTerritoryIdAndType(territory.getId(), RevisionType.OPO));
        modelAndView.addObject("gts", revisionRepository.findByTerritoryIdAndType(territory.getId(), RevisionType.GTS));
        modelAndView.addObject("headers", Revision.Companion.getHeaders());
        return modelAndView;
    }

    @RequestMapping("/revdelete")
    String removeRevision(@RequestParam("terId") Long terId, @RequestParam("revId") Long revId) {
        revisionRepository.delete(revId);
        return "redirect:/rev?id=" + Long.toString(terId);
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    void export(@RequestParam("terId") Long terId, @RequestParam("revType") int revType, HttpServletResponse response) throws Exception {
        RevisionType revisionType = RevisionType.values()[revType];
        List<Revision> list = revisionRepository.findByTerritoryIdAndType(terId, revisionType);
        ListContainer<Revision> container = new ListContainer<>(Revision.class);
        container.addAll(list);
        File file = excelExportService.export(container, revisionType);
        InputStream inputStream = new FileInputStream(file);
        response.setContentType(ExcelExportService.EXCEL_MIME_TYPE);
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
